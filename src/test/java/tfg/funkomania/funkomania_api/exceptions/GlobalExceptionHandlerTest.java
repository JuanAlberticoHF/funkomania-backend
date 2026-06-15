package tfg.funkomania.funkomania_api.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.*;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleUsuarioAlreadyExistsException() {
        UsuarioAlreadyExistsException ex = new UsuarioAlreadyExistsException("User exists");
        ProblemDetail pd = handler.handleException(ex);
        assertThat(pd.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(pd.getTitle()).isEqualTo("El usuario ya existe");
    }

    @Test
    void handleProductoNotFoundException() {
        ProductoNotFoundException ex = new ProductoNotFoundException("Not found");
        ProblemDetail pd = handler.handleException(ex);
        assertThat(pd.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(pd.getTitle()).isEqualTo("Producto no encontrado");
    }

    @Test
    void handleNullEmailAutenticationException() {
        NullEmailAutenticationException ex = new NullEmailAutenticationException("Null email");
        ProblemDetail pd = handler.handleException(ex);
        assertThat(pd.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(pd.getTitle()).isEqualTo("Error en la autenticación: email nulo");
    }

    @Test
    void handleUsuarioNotFoundException() {
        UsuarioNotFoundException ex = new UsuarioNotFoundException("Not found");
        ProblemDetail pd = handler.handleException(ex);
        assertThat(pd.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(pd.getTitle()).isEqualTo("Usuario no encontrado");
    }

    @Test
    void handleDireccionNotFoundException() {
        DireccionNotFoundException ex = new DireccionNotFoundException("Not found");
        ProblemDetail pd = handler.handleException(ex);
        assertThat(pd.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(pd.getTitle()).isEqualTo("Direccion no encontrada");
    }

    @Test
    void handleProductoYaEnListaDeseadosException() {
        ProductoYaEnListaDeseadosException ex = new ProductoYaEnListaDeseadosException("Already in list");
        ProblemDetail pd = handler.handleException(ex);
        assertThat(pd.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(pd.getTitle()).isEqualTo("Producto ya en lista de deseados");
    }

    @Test
    void handleNotificacionNotFoundException() {
        NotificacionNotFoundException ex = new NotificacionNotFoundException("Not found");
        ProblemDetail pd = handler.handleException(ex);
        assertThat(pd.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(pd.getTitle()).isEqualTo("Notificación no encontrada");
    }

    @Test
    void handleNotNotificationOwnerException() {
        NotNotificationOwnerException ex = new NotNotificationOwnerException("Not owner");
        ProblemDetail pd = handler.handleException(ex);
        assertThat(pd.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(pd.getTitle()).isEqualTo("No eres el propietario de esta notificación");
    }

    @Test
    void handleNotificacionYaLeidaException() {
        NotificacionYaLeidaException ex = new NotificacionYaLeidaException("Already read");
        ProblemDetail pd = handler.handleException(ex);
        assertThat(pd.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(pd.getTitle()).isEqualTo("La notificación ya ha sido leída");
    }

    @Test
    void handleCategoriaNotFoundException() {
        CategoriaNotFoundException ex = new CategoriaNotFoundException("Not found");
        ProblemDetail pd = handler.handleException(ex);
        assertThat(pd.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(pd.getTitle()).isEqualTo("Categoría no encontrada");
    }

    @Test
    void handleCategoriaConProductosException() {
        CategoriaConProductosException ex = new CategoriaConProductosException("Has products");
        ProblemDetail pd = handler.handleException(ex);
        assertThat(pd.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(pd.getTitle()).isEqualTo("No se puede eliminar la categoría porque tiene productos asociados");
    }

    @Test
    void handleProductoNoEliminadoException() {
        ProductoNoEliminadoException ex = new ProductoNoEliminadoException("Not deleted");
        ProblemDetail pd = handler.handleException(ex);
        assertThat(pd.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(pd.getTitle()).isEqualTo("No se pudo eliminar el producto");
    }

    @Test
    void handleCarritoNotFoundException() {
        CarritoNotFoundException ex = new CarritoNotFoundException("Not found");
        ProblemDetail pd = handler.handleException(ex);
        assertThat(pd.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(pd.getTitle()).isEqualTo("Carrito del usuario no existe.");
    }

    @Test
    void handleProductoNotFoundInCarritoException() {
        ProductoNotFoundInCarritoException ex = new ProductoNotFoundInCarritoException("Not in carrito");
        ProblemDetail pd = handler.handleException(ex);
        assertThat(pd.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(pd.getTitle()).isEqualTo("Producto no encontrado en el carrito del usuario.");
    }

    @Test
    void handlePedidoNotFoundException() {
        PedidoNotFoundException ex = new PedidoNotFoundException("Not found");
        ProblemDetail pd = handler.handleException(ex);
        assertThat(pd.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(pd.getTitle()).isEqualTo("Pedido no encontrado para el usuario.");
    }

    @Test
    void handleCarritoVacioException() {
        CarritoVacioException ex = new CarritoVacioException("Empty");
        ProblemDetail pd = handler.handleException(ex);
        assertThat(pd.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(pd.getTitle()).isEqualTo("No se puede realizar el pedido porque el carrito está vacío.");
    }

    @Test
    void handleMetodoPagoNotFoundException() {
        MetodoPagoNotFoundException ex = new MetodoPagoNotFoundException("Not found");
        ProblemDetail pd = handler.handleException(ex);
        assertThat(pd.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(pd.getTitle()).isEqualTo("Metodo de pago no encontrado para el usuario.");
    }

    @Test
    void handleDetallePedidoNotFoundException() {
        DetallePedidoNotFoundException ex = new DetallePedidoNotFoundException("Not found");
        ProblemDetail pd = handler.handleException(ex);
        assertThat(pd.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(pd.getTitle()).isEqualTo("Detalle de pedido no encontrado para el usuario.");
    }

    @Test
    void handleInsufficientStockException() {
        InsufficientStockException ex = new InsufficientStockException("No stock");
        ProblemDetail pd = handler.handleException(ex);
        assertThat(pd.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(pd.getTitle()).isEqualTo("No se puede realizar el pedido porque no hay suficiente stock del producto.");
    }

    @Test
    void handleCancelacionPedidoException() {
        CancelacionPedidoException ex = new CancelacionPedidoException("No cancel");
        ProblemDetail pd = handler.handleException(ex);
        assertThat(pd.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(pd.getTitle()).isEqualTo("No se puede cancelar el pedido");
    }

    @Test
    void handleAutenticacionFallidaException() {
        AutenticacionFallidaException ex = new AutenticacionFallidaException("Failed");
        ProblemDetail pd = handler.handleException(ex);
        assertThat(pd.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(pd.getTitle()).isEqualTo("No se pudo autenticar el usuario");
    }
}
