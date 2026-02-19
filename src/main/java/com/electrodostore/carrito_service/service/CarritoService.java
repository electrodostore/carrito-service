package com.electrodostore.carrito_service.service;

import com.electrodostore.carrito_service.dto.CarritoCreadoResponseDto;
import com.electrodostore.carrito_service.dto.CarritoResponseDto;
import com.electrodostore.carrito_service.dto.ClienteResponseDto;
import com.electrodostore.carrito_service.dto.ProductoResponseDto;
import com.electrodostore.carrito_service.exception.CarritoNotFoundException;
import com.electrodostore.carrito_service.integration.cliente.ClienteIntegrationService;
import com.electrodostore.carrito_service.integration.cliente.dto.ClienteIntegrationDto;
import com.electrodostore.carrito_service.model.Carrito;
import com.electrodostore.carrito_service.model.ClienteSnapshot;
import com.electrodostore.carrito_service.model.ProductoSnapshot;
import com.electrodostore.carrito_service.repository.ICarritoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.electrodostore.carrito_service.model.CarritoStatus.PENDING;

//Service del dominio donde se va a manejar toda la lógica de negocio
@Service
public class CarritoService implements ICarritoService {

    //Inyección de dependencia para el repositorio de carrito
    private final ICarritoRepository carritoRepo;
    //Inyección de dependencia para la integración con cliente-service
    private final ClienteIntegrationService clienteIntegration;

    //Inyección por método constructor
    public CarritoService(ICarritoRepository carritoRepo, ClienteIntegrationService clienteIntegration) {
        this.carritoRepo = carritoRepo;
        this.clienteIntegration = clienteIntegration;
    }

    //Método propio cuya función es preparar un Cliente para ser guardado como parte del registro de un carrito en la base de datos
    private ClienteSnapshot clienteIntegrationToSnapshot(ClienteIntegrationDto objCliente) {
        return new ClienteSnapshot(objCliente.getId(), objCliente.getName(), objCliente.getCellphone(),
                objCliente.getDocument(), objCliente.getAddress());
    }

    /*Método propio para preparar los datos de los diferentes productos de un carrito para que sean expuestos al cliente,
      esto se logra almacenandolos en objetos de transferencia de datos (DTOs) a partir del registro que tenemos de estos como Snapshot*/
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
    private CarritoResponseDto buildCarritoResponse(Carrito objCarrito){
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

        for(Carrito objCarrito: carritoRepo.findAll()){
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
}