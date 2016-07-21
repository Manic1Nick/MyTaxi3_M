package ua.artcode.taxi.aspect;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    @Before(value = "publicMethodsPointCut()")
    public void loggingPublicMethodsAdvice(){
        System.out.println("public method was called");
    }

    @Pointcut(value = "execution(public * ua.artcode.testio.service..*(..))")
    public void publicMethodsPointCut(){}



}
