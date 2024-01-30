package com.bintian.learn.mockito;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

public class CarTest {

    private AutoCloseable autoCloseable = null;
    @BeforeEach
    void init() {
         autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void releaseMock() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testCar() throws InvocationTargetException, IllegalAccessException {
        Method method = mock(Method.class, withSettings().verboseLogging());
        //when(method.invoke(new Object(), null)).thenThrow(new RuntimeException("tianbin test"));
        Car car = mock(Car.class, withSettings().verboseLogging());
        when(car.getErrorCode()).thenReturn("test");
        String errorCode = car.getErrorCode();
        Assertions.assertEquals(errorCode, "test");
    }
}
