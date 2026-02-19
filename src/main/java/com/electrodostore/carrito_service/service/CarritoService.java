package com.electrodostore.carrito_service.service;

import com.electrodostore.carrito_service.dto.CarritoCreadoResponseDto;
import com.electrodostore.carrito_service.integration.cliente.ClienteIntegrationService;
import com.electrodostore.carrito_service.integration.cliente.dto.ClienteIntegrationDto;
import com.electrodostore.carrito_service.model.Carrito;
import com.electrodostore.carrito_service.model.ClienteSnapshot;
import com.electrodostore.carrito_service.repository.ICarritoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.electrodostore.carrito_service.model.CarritoStatus.PENDING;

//Service del dominio donde se va a manejar toda la lógica de negocio
@Service
public class CarritoService implements ICarritoService{

    //Inyección de dependencia para el repositorio de carrito
    private final ICarritoRepository carritoRepo;
    //Inyección de dependencia para la integración con cliente-service
    private final ClienteIntegrationService clienteIntegration;
    //Inyección por método constructor
    public CarritoService(ICarritoRepository carritoRepo, ClienteIntegrationService clienteIntegration){
        this.carritoRepo = carritoRepo;
        this.clienteIntegration = clienteIntegration;
    }

    //Método propio cuya función es preparar un Cliente para ser guardado como parte del registro de un carrito en la base de datos
    private ClienteSnapshot clienteIntegrationToSnapshot(ClienteIntegrationDto objCliente){
        return new ClienteSnapshot(objCliente.getId(), objCliente.getName(), objCliente.getCellphone(),
                objCliente.getDocument(), objCliente.getAddress());
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
