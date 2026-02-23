package com.electrodostore.carrito_service.service;

import com.electrodostore.carrito_service.dto.*;
import com.electrodostore.carrito_service.exception.CarritoNotFoundException;
import com.electrodostore.carrito_service.exception.ProductoNotFoundException;
import com.electrodostore.carrito_service.integration.cliente.ClienteIntegrationService;
import com.electrodostore.carrito_service.integration.cliente.dto.ClienteIntegrationDto;
import com.electrodostore.carrito_service.integration.producto.ProductoIntegrationService;
import com.electrodostore.carrito_service.integration.producto.dto.ProductoIntegrationDto;
import com.electrodostore.carrito_service.model.Carrito;
import com.electrodostore.carrito_service.model.ClienteSnapshot;
import com.electrodostore.carrito_service.model.ProductoSnapshot;
import com.electrodostore.carrito_service.repository.ICarritoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static com.electrodostore.carrito_service.model.CarritoStatus.PENDING;

//Service del dominio donde se va a manejar toda la lógica de negocio
@Service
public class CarritoService implements ICarritoService {

    //Inyección de dependencia para el repositorio de carrito
    private final ICarritoRepository carritoRepo;
    //Inyección de dependencia para la integración con cliente-service
    private final ClienteIntegrationService clienteIntegration;
    private final ProductoIntegrationService productoIntegration;

    //Inyección por método constructor
    public CarritoService(ICarritoRepository carritoRepo, ClienteIntegrationService clienteIntegration, ProductoIntegrationService productoIntegration) {
        this.carritoRepo = carritoRepo;
        this.clienteIntegration = clienteIntegration;
        this.productoIntegration = productoIntegration;
    }

    //Método propio para hacer la integración con productoService y consultar uno o varios productos por su id
    //En caso de que ocurran problemas en la integración (algún producto no fue encontrado, no se mandó a consultar nada, etc) también se manejarán aquí
    private List<ProductoIntegrationDto> findProductos(List<Long> productosIds) {

        //En caso de que no se mande ningún ID de producto en la lista, no podemos hacer la integración
        if (productosIds.isEmpty()) {throw new ProductoNotFoundException("Ningún producto fue mandado a consultar");}

        //Si solo se manda a consultar un producto, no es necesario usar el método de integración que consulta una lista de estos
        if (productosIds.size() == 1) {return List.of(productoIntegration.findProducto(productosIds.get(0)));}

        //Si son varios los productos sacamos la lista de estos
        List<ProductoIntegrationDto> productosIntegration = productoIntegration.findProductos(productosIds);

        //Comparamos longitudes de la lista de ids con la lista de productos que llegaron en la integración para determinar si llegaron todos
        if (productosIntegration.size() < productosIds.size()) {throw new ProductoNotFoundException("Uno o varios productos no fueron encontrado");}

        //Finalmente, si todo está ok -> Retornamos lista
        return productosIntegration;
    }

    //Método propio para sacar los ids de una lista de productos que se quieren agregar a un carrito
    private List<Long> sacarIdsProductos(List<ProductoAgregarDto> listProductos) {
        //Lista donde se almacenan los ids
        List<Long> productosIds = new ArrayList<>();

        //Recorremos los productos y vamos agregando cada id de cada producto
        for (ProductoAgregarDto objProducto : listProductos) {
            productosIds.add(objProducto.getId());
        }

        return productosIds;
    }

    /*Método propio para filtrar los productos que ya se habían agregado al carrito para que en vez de agregarlos otra vez
    *  se le sume la cantidad y el subtotal al producto que ya estaba registrado dentro del carrito*/
    private List<ProductoSnapshot> filtrarProductosExistentes(Carrito carrito, List<ProductoSnapshot> productosAgregar){
        //Aquí se van a almacenar los productos que no estén ya dentro del carrito
        List<ProductoSnapshot> productosNuevos = new ArrayList<>();

        //Se recorre la lista de los productos que se quieren agregar al carrito para irlos comparando con los que ya estaban
        for(ProductoSnapshot productoAgregar: productosAgregar){

            //Creamos variable booleana para saber, una vez se salga del bucle siguiente, si se encontró o no coincidencia con productoAgregar
            boolean validacionCoincidencia  = false;

            //Recorremos ahora la lista de los productos que están dentro del carrito para poder comparar
            for(ProductoSnapshot productoCarrito: carrito.getListProductos()){

                //Si encontramos coincidencia entre el producto que se quiere agregar y uno que ya estaba en el carrito, hacemos los cambios en el carrito
                if(productoAgregar.getProductId().equals(productoCarrito.getProductId())){

                    //A la cantidad comprada que ya estaba registrada se le suma la cantidad nueva que se quiere agregar
                    productoCarrito.setPurchasedQuantity(
                            productoCarrito.getPurchasedQuantity() + productoAgregar.getPurchasedQuantity()
                    );

                    //Al subtotal del producto que ya estaba en el carrito se le suma el subtotal nuevo que se quiere agregar
                    productoCarrito.setSubTotal(
                            //Como los subtotales son formato BigDecimal, toca sumarlos con el método add() de esa clase
                            productoCarrito.getSubTotal().add(productoAgregar.getSubTotal())
                    );

                    //Cambiamos la validación a true, ya que encontramos coincidencia
                    validacionCoincidencia = true;

                    //Si ya encontramos la coincidencia no tiene sentido seguir buscando
                    break;
                }
            }

            //Con la ayuda de validacionCoincidencia determinamos si la salida del bucle fue porque se encontró la coincidencia (true) o no se encontró (false)
            if(!validacionCoincidencia){productosNuevos.add(productoAgregar);}
        }

        //Al final, retornamos la lista con los productos que si son realmente nuevos
        return productosNuevos;
    }

    /*Método propio para transferir los datos de una lista de productos (que se integraron desde producto-service a este
         servicio) a una lista de objetos Snapshot para su posterior persistencia en la base de datos*/
    /*Para esto necesitamos la lista de los productos que se integraron (productosIntegration) y la lista con el id la
     cantidad que se quiere comprar de cada producto (productosAgregados) */
    private List<ProductoSnapshot> productosIntegrationToSnapshot(List<ProductoIntegrationDto> productosIntegration, List<ProductoAgregarDto> productosAgregar) {
        //Lista de Snapshots para los productos que se integraron
        List<ProductoSnapshot> productosSnapshot = new ArrayList<>();

        //Se recorre la lista de los productos que se quieren agregar al carrito (estos objetos contienen en el ID y la cantidad que se desea comprar del producto)
        for (ProductoAgregarDto objProductoAgregar : productosAgregar) {

            //Ahora, recorremos la lista de los productos que se integraron, estos deben ser equivalentes a los que se quiere agregar
            for (ProductoIntegrationDto objProductoIntegration : productosIntegration) {
                //Comparamos por ID cada producto para encontrar las coincidencias
                if (objProductoAgregar.getId().equals(objProductoIntegration.getId())) {
                    //Primero verificamos si el stock del producto es suficiente para la cantidad que se quiere comprar
                    productoIntegration.verificarProductoStock(objProductoIntegration.getId(), objProductoAgregar.getQuantity());

                    //Se calcula el subTotal de cada producto comprado en formato BigDecimal
                    BigDecimal subTotal = objProductoIntegration.getPrice().multiply(BigDecimal.valueOf(objProductoAgregar.getQuantity()));

                    //Creamos la instancia del objeto Snapshot con base a los datos de ambos objetos (objProductoIntegration y objProductoAgregar)
                    productosSnapshot.add(new ProductoSnapshot(objProductoAgregar.getId(), objProductoIntegration.getName(), objProductoIntegration.getPrice(),
                            objProductoAgregar.getQuantity(), subTotal,
                            objProductoIntegration.getDescription()));
                }
            }
        }

        return productosSnapshot;
    }

    //Método propio cuya función es preparar un Cliente para ser guardado como parte del registro de un carrito en la base de datos
    private ClienteSnapshot clienteIntegrationToSnapshot(ClienteIntegrationDto objCliente) {
        return new ClienteSnapshot(objCliente.getId(), objCliente.getName(), objCliente.getCellphone(),
                objCliente.getDocument(), objCliente.getAddress());
    }

    /*Método propio para preparar los datos de los diferentes productos de un carrito para que sean expuestos al cliente,
      esto se logra almacenándolos en objetos de transferencia de datos (DTOs) a partir del registro que tenemos de estos como Snapshot*/
    private List<ProductoResponseDto> productosSnapshotToResponse(List<ProductoSnapshot> listProductos) {

        //Se crea la lista de los DTOs
        List<ProductoResponseDto> listProductosResponse = new ArrayList<>();

        //Se recorre la lista de los productos provenientes de la base de datos y vamos transfiriendo los datos de cada uno
        for (ProductoSnapshot objProducto : listProductos) {
            //Vamos surtiendo la lista con los datos de cada producto
            listProductosResponse.add(new ProductoResponseDto(objProducto.getProductId(), objProducto.getProductName(),
                    objProducto.getProductPrice(), objProducto.getPurchasedQuantity(), objProducto.getSubTotal(),
                    objProducto.getProductDescription()));
        }

        return listProductosResponse;
    }

    //Método propio para calcular el total de un carrito a partir de los productos que tenga dentro
    private BigDecimal calcularTotalCarrito(List<ProductoSnapshot> listProductos){
        BigDecimal total = BigDecimal.ZERO;

        for(ProductoSnapshot objProducto: listProductos){
            //Para sumar BigDecimal debe ser por el método add() de la clase
            total = total.add(objProducto.getSubTotal());
        }

        return total;
    }

    //Método propio para consultar un carrito por su id. En caso de que no exista lo notificamos con la respectiva excepción de dominio
    private Carrito findCarrito(Long carritoId) {
        //Guardamos objeto en un optional para evitar el null en caso de que no exista el carrito
        Optional<Carrito> objCarrito = carritoRepo.findById(carritoId);

        //Si el optional está vacío = no existe carrito --> Excepción CarritoNotFound
        if (objCarrito.isEmpty()) {
            throw new CarritoNotFoundException("No se encontró carrito con id: " + carritoId);
        }

        return objCarrito.get();
    }

    /*Método propio para preparar los datos de un Cliente dueño de un carrito (CarritoSnapshot) para ser expuesto
      a partir del registro que tenemos de este como Snapshot*/
    private ClienteResponseDto clienteSnapshotToResponse(ClienteSnapshot objCliente) {
        return new ClienteResponseDto(objCliente.getClientId(), objCliente.getClientName(), objCliente.getClientCellphone(),
                objCliente.getClientDocument(), objCliente.getClientAddress());
    }

    //Método propio para sacar una instancia de la clase DTO que me expone los datos de un carrito al cliente
    private CarritoResponseDto buildCarritoResponse(Carrito objCarrito) {
        //Sacamos objeto para la transferencia de los datos de un carrito (DTO)
        return new CarritoResponseDto(
                objCarrito.getId(),
                productosSnapshotToResponse(new ArrayList<>(objCarrito.getListProductos())),
                clienteSnapshotToResponse(objCarrito.getCliente()),
                objCarrito.getStatus()
        );
    }

    @Transactional(readOnly = true)
    @Override
    public List<CarritoResponseDto> findAllCarritos() {
        //Lista de DTOs de los carritos
        List<CarritoResponseDto> listCarritos = new ArrayList<>();

        for (Carrito objCarrito : carritoRepo.findAll()) {
            //Vamos transfiriendo los datos de los carritos a los objetos DTO y agregando a la lista
            listCarritos.add(buildCarritoResponse(objCarrito));
        }

        return listCarritos;
    }

    @Transactional(readOnly = true)
    @Override
    public CarritoResponseDto findCarritoResponse(Long carritoId) {
        return buildCarritoResponse(
                //Buscamos el carrito y transferimos los datos de este al DTO para exponerlo
                findCarrito(carritoId)
        );
    }

    @Transactional
    @Override
    public CarritoCreadoResponseDto crearCarrito(Long clienteId) {
        //Se crea el nuevo carrito que será registrado, en principio, vacío
        Carrito objCarrito = new Carrito();

        //Hacemos la integración del servicio Cliente con Carrito-service consultado el cliente del carrito por su ID
        //Si se da algún problema en esta integración, este se maneja en la capa "integration"
        ClienteIntegrationDto cliente = clienteIntegration.findCliente(clienteId);

        //Preparamos el cliente para su persistencia en la base de datos
        objCarrito.setCliente(clienteIntegrationToSnapshot(cliente));

        //Le asignamos el estado al carrito que al momento de la creación es: PENDING (pendiente)
        objCarrito.setStatus(PENDING);

        //Guardamos el registro del carrito
        carritoRepo.save(objCarrito);

        //Retornamos los datos del carrito relevante para su posterior manejo
        return new CarritoCreadoResponseDto(objCarrito.getId(), objCarrito.getStatus());
    }

    @Transactional
    @Override
    public CarritoResponseDto agregarProductos(Long carritoId, List<ProductoAgregarDto> productosAgregar) {
        //Buscamos el carrito para confirmamos que existe
        Carrito objCarrito = findCarrito(carritoId);

        //sacamos los ids de los productos que se quieren agregar y buscamos los productos
        List<ProductoIntegrationDto> productosIntegration = findProductos(
                 sacarIdsProductos(productosAgregar)
        );

        //Con los datos de los productos que se integraron construimos los Snapshots para persistirlos en la base de datos
        List<ProductoSnapshot> productosSnapshot = productosIntegrationToSnapshot(productosIntegration, productosAgregar);

        /*Antes de agregar los productos a la lista de productos del carrito, debemos verificar si alguno no se encuentra
         ya dentro de este. Para esto usamos el método "filtrarProductosExistentes"*/
        List<ProductoSnapshot> productosNuevos = filtrarProductosExistentes(objCarrito, productosSnapshot);

        //Agregamos los nuevos productos al carrito
        for(ProductoSnapshot productoSnapshot: productosNuevos){
            objCarrito.getListProductos().add(productoSnapshot);
        }

        //Actualizamos cambios en la base de datos
        carritoRepo.save(objCarrito);

        //Preparamos el carrito para ser expuesto con los nuevos productos
        return buildCarritoResponse(objCarrito);
    }

}