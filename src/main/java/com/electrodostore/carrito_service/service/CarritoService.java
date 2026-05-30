package com.electrodostore.carrito_service.service;

import com.electrodostore.carrito_service.dto.*;
import com.electrodostore.carrito_service.exception.CarritoNotFoundException;
import com.electrodostore.carrito_service.exception.ProductoNotFoundException;
import com.electrodostore.carrito_service.exception.UnauthorizedOperationException;
import com.electrodostore.carrito_service.integration.cliente.ClienteIntegrationService;
import com.electrodostore.carrito_service.integration.cliente.dto.ClienteIntegrationDto;
import com.electrodostore.carrito_service.integration.producto.ProductoIntegrationService;
import com.electrodostore.carrito_service.integration.producto.dto.ProductoIntegrationDto;
import com.electrodostore.carrito_service.integration.producto.dto.ProductoIntegrationStockDto;
import com.electrodostore.carrito_service.integration.venta.VentaIntegrationService;
import com.electrodostore.carrito_service.integration.venta.dto.ProductoIntegrationRequestDto;
import com.electrodostore.carrito_service.integration.venta.dto.VentaIntegrationResponseDto;
import com.electrodostore.carrito_service.model.Carrito;
import com.electrodostore.carrito_service.model.ClienteSnapshot;
import com.electrodostore.carrito_service.model.ProductoSnapshot;
import com.electrodostore.carrito_service.repository.ICarritoRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static com.electrodostore.carrito_service.model.CarritoStatus.PENDING;
import static com.electrodostore.carrito_service.model.CarritoStatus.PURCHASED;

@Service
public class CarritoService implements ICarritoService {

    private final ICarritoRepository carritoRepo;
    private final ClienteIntegrationService clienteIntegration;
    private final ProductoIntegrationService productoIntegration;
    private final VentaIntegrationService ventaIntegration;

    public CarritoService(ICarritoRepository carritoRepo, ClienteIntegrationService clienteIntegration, ProductoIntegrationService productoIntegration, VentaIntegrationService ventaIntegration) {
        this.carritoRepo = carritoRepo;
        this.clienteIntegration = clienteIntegration;
        this.productoIntegration = productoIntegration;
        this.ventaIntegration = ventaIntegration;
    }

    /**
     * Consulta datos de productos en producto-service
     * */
    private List<ProductoIntegrationDto> findProductos(List<Long> productosIds) {

        if (productosIds.isEmpty()) {throw new ProductoNotFoundException("Ningún producto fue mandado a consultar");}

        if (productosIds.size() == 1) {return List.of(productoIntegration.findProducto(productosIds.get(0)));}

        List<ProductoIntegrationDto> productosIntegration = productoIntegration.findProductos(productosIds);

        if (productosIntegration.size() < productosIds.size()) {throw new ProductoNotFoundException("Uno o varios productos no fueron encontrado");}

        return productosIntegration;
    }

    /**
     * Obtiene los ids de los productos que se quieren agregar al carrito
     */
    private List<Long> sacarIdsProductos(List<ProductoAgregarDto> listProductos) {
        List<Long> productosIds = new ArrayList<>();

        for (ProductoAgregarDto objProducto : listProductos) {
            productosIds.add(objProducto.id());
        }

        return productosIds;
    }

    /**
     * Encuentra productos que ya estaban almacenados en el carrito
     * y actualiza cantidad y subtotal evitando perder consistencia
     * al ingresarlos de nuevo
     * */
    private List<ProductoSnapshot> filtrarProductosExistentes(Carrito carrito, List<ProductoSnapshot> productosAgregar){
        List<ProductoSnapshot> productosNuevos = new ArrayList<>();

        for(ProductoSnapshot productoAgregar: productosAgregar){

            boolean validacionCoincidencia  = false;

            for(ProductoSnapshot productoCarrito: carrito.getListProductos()){

                if(productoAgregar.getProductId().equals(productoCarrito.getProductId())){

                    productoCarrito.setPurchasedQuantity(
                            productoCarrito.getPurchasedQuantity() + productoAgregar.getPurchasedQuantity()
                    );

                    productoCarrito.setSubTotal(
                            productoCarrito.getSubTotal().add(productoAgregar.getSubTotal())
                    );

                    validacionCoincidencia = true;

                    //Deja de buscar cuando se encuentra coincidencia
                    break;
                }
            }

            //Agrega los productos que no hacían parte del carrito
            if(!validacionCoincidencia){
                productosNuevos.add(productoAgregar);
            }
        }

        return productosNuevos;
    }

    /**
     * Construye DTOs para validar stock de productos.
     */
    private List<ProductoIntegrationStockDto> buildProductosIntegration(List<? extends ProductoConCantidadDto> listProductos){

        List<ProductoIntegrationStockDto> productosIntegration = new ArrayList<>();

        for(ProductoConCantidadDto objProducto: listProductos){

            productosIntegration.add(
                    new ProductoIntegrationStockDto(
                            objProducto.getProductoId(), objProducto.getCantidad())
            );
        }

        return productosIntegration;
    }

    /**
     * Valida stock de productos en producto-service
     */
    private void validarProductosStock(List<? extends ProductoConCantidadDto> productosValidarStock){
        List<ProductoIntegrationStockDto> productosIntegration = buildProductosIntegration(
                productosValidarStock
        );

        productoIntegration.verificarProductosStock(productosIntegration);
    }

    /**
     * Construye Snapshots para persistencia a partir
     * de productos integrados desde producto-service
     * */
    private List<ProductoSnapshot> productosIntegrationToSnapshot(List<ProductoIntegrationDto> productosIntegration, List<ProductoAgregarDto> productosAgregar) {

        validarProductosStock(productosAgregar);

        List<ProductoSnapshot> productosSnapshot = new ArrayList<>();

        /* Relaciona cada producto obtenido desde producto-service
         * con su correspondiente solicitud para construir el snapshot.
         */
        for (ProductoAgregarDto objProductoAgregar : productosAgregar) {

            for (ProductoIntegrationDto objProductoIntegration : productosIntegration) {
                //Busca la coincidencia y construye Snapshot
                if (objProductoAgregar.id().equals(objProductoIntegration.getId())) {

                    BigDecimal subTotal = objProductoIntegration.getPrice().multiply(
                            BigDecimal.valueOf(objProductoAgregar.quantity())
                    );

                    productosSnapshot.add(
                            new ProductoSnapshot(
                                    objProductoAgregar.id(),
                                    objProductoIntegration.getName(),
                                    objProductoIntegration.getPrice(),
                                    objProductoAgregar.quantity(), subTotal,
                                    objProductoIntegration.getDescription())
                    );
                }
            }
        }

        return productosSnapshot;
    }

    /**
     * Construye Snapshot de un cliente para persistir.
     */
    private ClienteSnapshot clienteIntegrationToSnapshot(ClienteIntegrationDto objCliente) {
        return new ClienteSnapshot(
                objCliente.getId(),
                objCliente.getName(),
                objCliente.getCellphone(),
                objCliente.getDocument(),
                objCliente.getAddress()
        );
    }

    /**
     * Construye DTO de respuesta para productos.
     */
    private List<ProductoResponseDto> productosSnapshotToResponse(List<ProductoSnapshot> listProductos) {

        List<ProductoResponseDto> listProductosResponse = new ArrayList<>();

        for (ProductoSnapshot objProducto : listProductos) {
            listProductosResponse.add(
                    new ProductoResponseDto(
                            objProducto.getProductId(),
                            objProducto.getProductName(),
                            objProducto.getProductPrice(),
                            objProducto.getPurchasedQuantity(),
                            objProducto.getSubTotal(),
                            objProducto.getProductDescription()));
        }

        return listProductosResponse;
    }

    /**
     * Construye DTO de productos de integración
     * para registrar una venta.
     * */
    private List<ProductoIntegrationRequestDto> productoSnapshotToIntegration(List<ProductoSnapshot> productosSnapshot){
        List<ProductoIntegrationRequestDto> productosIntegration = new ArrayList<>();

        for(ProductoSnapshot productoSnapshot: productosSnapshot){
            productosIntegration.add(
                    new ProductoIntegrationRequestDto(
                            productoSnapshot.getProductId(),
                            productoSnapshot.getPurchasedQuantity())
            );
        }

        //Finalmente, devolvemos la lista construida de productos integration
        return productosIntegration;
    }

    private BigDecimal calcularTotalCarrito(Set<ProductoSnapshot> listProductos){
        BigDecimal total = BigDecimal.ZERO;

        for(ProductoSnapshot objProducto: listProductos){
            total = total.add(objProducto.getSubTotal());
        }

        return total;
    }

    /**
     * Busca carrito por su ID y lanza excepción
     * de dominio si no existe
     * */
    private Carrito findCarrito(Long carritoId) {
        Optional<Carrito> objCarrito = carritoRepo.findById(carritoId);

        //Si el optional está vacío = no existe carrito --> Excepción CarritoNotFound
        if (objCarrito.isEmpty()) {
            throw new CarritoNotFoundException("No se encontró carrito con id: " + carritoId);
        }

        return objCarrito.get();
    }

    /**
     * Construye DTO de respuesta para cliente
     */
    private ClienteResponseDto clienteSnapshotToResponse(ClienteSnapshot objCliente) {
        return new ClienteResponseDto(
                objCliente.getClientId(),
                objCliente.getClientName(),
                objCliente.getClientCellphone(),
                objCliente.getClientDocument(),
                objCliente.getClientAddress()
        );
    }

    private boolean validarProductoEnCarrito(Set<ProductoSnapshot> productosCarrito, Long productoId){

        //Busca el producto en el carrito y devuelve true si se encuentra
        for(ProductoSnapshot objProducto: productosCarrito){

            if(objProducto.getProductId().equals(productoId)){
                return true;
            }
        }
        return false;
    }

    /**
     * Construye DTO de respuesta para un carrito
     * */
    private CarritoResponseDto buildCarritoResponse(Carrito objCarrito) {

        return new CarritoResponseDto(
                objCarrito.getId(),
                objCarrito.getTotal(),
                productosSnapshotToResponse(new ArrayList<>(
                        objCarrito.getListProductos())
                ),
                clienteSnapshotToResponse(objCarrito.getCliente()),
                objCarrito.getStatus()
        );
    }

    /**
     * Extrae la identidad del cliente
     * autenticado y retorna su id.
     */
    private Long getAuthenticatedClientId(){
        //Busca la autenticación actual del usuario
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //Recupera claims del token
        Jwt principal = (Jwt) authentication.getPrincipal();

        //Busca identidad comercial del usuario
        Number clientId = principal.getClaim("clientId");

        //Valida que el usuario realmente sea cliente
        if(clientId == null){throw new UnauthorizedOperationException("El usuario no es cliente, por lo que " +
                "no puede realizar la operación");
        }

        return  clientId.longValue();

    }

    /**
     * Busca el carrito pendiente del cliente autenticado, o retorna
     * excepción en caso de que no exista
     */
    private Carrito findCarritoPending(){

        return carritoRepo.findByCliente_clientIdAndStatus(
                getAuthenticatedClientId(),
                PENDING
        ).orElseThrow(
                () -> new CarritoNotFoundException("El cliente no tiene carritos pendientes, agregue un producto para crear uno")
        );

    }

    @Transactional(readOnly = true)
    @Override
    public List<CarritoResponseDto> findAllCarritos() {
        List<CarritoResponseDto> listCarritos = new ArrayList<>();

        for (Carrito objCarrito : carritoRepo.findAll()) {
            listCarritos.add(
                    buildCarritoResponse(objCarrito)
            );
        }

        return listCarritos;
    }

    @Transactional(readOnly = true)
    @Override
    public CarritoResponseDto findCarritoResponse(Long carritoId) {
        return buildCarritoResponse(
                findCarrito(carritoId)
        );
    }

    @Transactional(readOnly = true)
    @Override
    public CarritoResponseDto findMyCarritoPending() {
        return buildCarritoResponse(
                findCarritoPending()
        );
    }

    @Transactional
    @Override
    public Carrito crearCarrito() {
        Carrito objCarrito = new Carrito();

        //Obtiene el cliente autenticado para asociarlo al carrito.
        ClienteIntegrationDto cliente = clienteIntegration.findCliente(
                getAuthenticatedClientId()
        );

        objCarrito.setCliente(clienteIntegrationToSnapshot(cliente));

        //Asigna estado inicial al carrito
        objCarrito.setStatus(PENDING);

        return carritoRepo.save(objCarrito);
    }

    @Transactional
    @Override
    public CarritoResponseDto agregarProductos(List<ProductoAgregarDto> productosAgregar) {
        /*Busca el carrito con estado pendiente del
          cliente autenticado y caso de que no existe crea uno nuevo*/
        Carrito carritoPending = carritoRepo.findByCliente_clientIdAndStatus(
                getAuthenticatedClientId(),
                PENDING
        ).orElseGet(
                this::crearCarrito
        );

        //Busca e integra los productos solicitados
        List<ProductoIntegrationDto> productosIntegration = findProductos(
                 sacarIdsProductos(productosAgregar)
        );

        List<ProductoSnapshot> productosSnapshot = productosIntegrationToSnapshot(
                productosIntegration, productosAgregar
        );


        //Filtra los productos que no hacen parte del carrito
        List<ProductoSnapshot> productosNuevos = filtrarProductosExistentes(
                carritoPending, productosSnapshot
        );

        for(ProductoSnapshot productoSnapshot: productosNuevos){
            carritoPending.getListProductos().add(productoSnapshot);
        }

        carritoPending.setTotal(
                calcularTotalCarrito(carritoPending.getListProductos())
        );

        return buildCarritoResponse(carritoPending);
    }

    @Transactional
    @Override
    public CarritoResponseDto deleteProductos(Long productoEliminarId) {

        Carrito carritoPending = findCarritoPending();

        //Validamos que el producto si exista en el carrito
        if(!(
                validarProductoEnCarrito(carritoPending.getListProductos(), productoEliminarId))
        ){
            throw new ProductoNotFoundException("No existe producto con id: " +productoEliminarId + " en el carrito");
        }

        //Conserva los productos no eliminados
        Set<ProductoSnapshot> productosSobrevivientes = carritoPending.getListProductos();

        /*Busca coincidencia y remueve el producto de la lista auxiliar para
             evitar modificar la colección que se esté recorriendo*/
        for(ProductoSnapshot objProducto: carritoPending.getListProductos()){

            if(objProducto.getProductId().equals(productoEliminarId)){
                productosSobrevivientes.remove(objProducto);

                //Deja de buscar cuando se encuentra coincidencia
                break;
            }
        }

        carritoPending.setListProductos(
                productosSobrevivientes
        );

        //Calcula el nuevo total del carrito
        carritoPending.setTotal(
                calcularTotalCarrito(productosSobrevivientes)
        );

        return buildCarritoResponse(carritoPending);
    }

    @Transactional
    @Override
    public CarritoResponseDto cambiarCantidadProducto(ProductoCambiarCantidadDto productoNuevaCantidad){

        Carrito carritoPending = findCarritoPending();

        //Validamos que el producto exista en el carrito.
        if(!(
                validarProductoEnCarrito(carritoPending.getListProductos(), productoNuevaCantidad.getProductoId()))
        ){
            throw new ProductoNotFoundException("No existe producto con id: " + productoNuevaCantidad.getProductoId() + " en el carrito");
        }

        //Valida la nueva cantidad
        validarProductosStock(
                List.of(productoNuevaCantidad)
        );

        for(ProductoSnapshot objProducto: carritoPending.getListProductos()){

            //Buscamos producto para actualizar cantidad comprada
            if(objProducto.getProductId().equals(productoNuevaCantidad.getProductoId())){

                objProducto.setPurchasedQuantity(
                        productoNuevaCantidad.newQuantity()
                );

               //Le da formato a la nueva cantidad y calcula nuevo subtotal
                objProducto.setSubTotal(
                        objProducto.getProductPrice().multiply(
                                BigDecimal.valueOf(productoNuevaCantidad.newQuantity()))
                );

                //Deja de buscar cuando se encuentra coincidencia
                break;
            }
        }

        carritoPending.setTotal(
                calcularTotalCarrito(carritoPending.getListProductos())
        );

        return buildCarritoResponse(carritoPending);
    }

    @Transactional
    @Override
    public VentaIntegrationResponseDto comprarCarrito() {
        Carrito carritoPending = findCarritoPending();

        //Construye DTO de productos para integración con venta-service
        List<ProductoIntegrationRequestDto> productosRequestVenta = productoSnapshotToIntegration(
                new ArrayList<>(
                        carritoPending.getListProductos()
                )
        );

        VentaIntegrationResponseDto ventaResponse = ventaIntegration.createVenta(
                productosRequestVenta
        );

        carritoPending.setStatus(PURCHASED);

        return ventaResponse;
    }

    @Transactional
    @Override
    public void vaciarMiCarrito() {
        Carrito carritoPending = findCarritoPending();

        carritoPending.getListProductos().clear();

        //Actualiza el total
        carritoPending.setTotal(BigDecimal.ZERO);
    }

}