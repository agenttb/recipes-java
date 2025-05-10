package com.bintian.learn.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.function.ThrowingSupplier;

import java.util.Arrays;

public class MethodInvokeUtil {
    private static final Logger logger = LoggerFactory.getLogger(MethodInvokeUtil.class);
    /**
     * Invokes a ThrowingSupplier and logs the input parameters.
     *
     * @param <T> the type of result supplied
     * @param supplier the ThrowingSupplier to invoke
     * @param inputs description of input parameters to log
     * @return the result of the supplier
     * @throws Exception if the supplier throws an exception
     */
    public static <T> T invokeAndLog(ThrowingSupplier<T> supplier, Object... inputs) {
        T result;
        logger.info("Invoking ThrowingSupplier with inputs: {}", Arrays.toString(inputs));
        try {
            result = supplier.get();
        } catch (Exception e) {
            logger.error("Error occurred while invoking ThrowingSupplier with inputs: {}", Arrays.toString(inputs), e);
            throw e;
        }
        return result;
    }

    public static void main(String[] args) {
        int a = 10;
        int b = 20;
        MethodInvokeUtil.invokeAndLog(() -> a +b, a , b);
    }
}
