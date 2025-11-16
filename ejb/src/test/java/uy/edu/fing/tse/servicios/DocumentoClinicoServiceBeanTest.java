package uy.edu.fing.tse.servicios;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uy.edu.fing.tse.entidades.DocumentoClinico;
import uy.edu.fing.tse.api.DocumentoClinicoPerLocal;

@ExtendWith(MockitoExtension.class)
public class DocumentoClinicoServiceBeanTest {

    @Mock
    private DocumentoClinicoPerLocal per;

    @InjectMocks
    private DocumentoClinicoServiceBean service;

    @Test
    void crear_nullCodigo_throws() {
        DocumentoClinico d = new DocumentoClinico();
        d.setCodigo(null);
        d.setPacienteCI("123");
        d.setFechaEmision(LocalDate.now());

        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> service.crear(d));
        assertNotNull(ex1);
    }

    @Test
    void crear_codigoExistente_throws() {
        DocumentoClinico d = new DocumentoClinico();
        d.setCodigo("C1");
        d.setPacienteCI("123");
        d.setFechaEmision(LocalDate.now());

        when(per.existeCodigo("C1")).thenReturn(true);
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> service.crear(d));
        assertNotNull(ex2);
    }

    @Test
    void buscarPorPacienteCI_filtersCorrectly() {
        DocumentoClinico a = new DocumentoClinico(); a.setPacienteCI("111");
        DocumentoClinico b = new DocumentoClinico(); b.setPacienteCI("222");
        when(per.listar()).thenReturn(List.of(a,b));

        List<DocumentoClinico> out = service.buscarPorPacienteCI("111");
        assertEquals(1, out.size());
        assertEquals("111", out.get(0).getPacienteCI());
    }
}
