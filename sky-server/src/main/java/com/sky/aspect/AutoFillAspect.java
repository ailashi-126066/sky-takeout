package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.aspectj.lang.reflect.MethodSignature;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

//自定义切面
@Aspect
@Component
@Slf4j
public class AutoFillAspect
{
    @Pointcut("execution(* com.sky.mapper.*.*(..))  && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut()
    {

    }

    //前置通知，为公共字段赋值
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        log.info("开始为公共字段赋值");
        //获取数据库操作类型
        MethodSignature signature=(MethodSignature)joinPoint.getSignature();
        AutoFill autoFill= signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType=autoFill.value();
        //获取实体
        Object[] args=joinPoint.getArgs();
        if(args.length==0 || args==null)
        {
            return;
        }
        Object entity=args[0];
        //准备赋值的数据
        LocalDate date=LocalDate.now();
        Long currentId= BaseContext.getCurrentId();
        //通过反射为公共字段赋值
        if(operationType==OperationType.INSERT)
        {
            Method setCreateTime=entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
            Method setUpdateTime=entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setCreateUser=entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
            Method setUpdateUser=entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

            //为公共字段赋值
            setCreateTime.invoke(entity,LocalDateTime.now());
            setUpdateTime.invoke(entity,LocalDateTime.now());
            setCreateUser.invoke(entity,currentId);
            setUpdateUser.invoke(entity,currentId);

        } else if (operationType==OperationType.UPDATE) {
            Method setUpdateTime=entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setUpdateUser=entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
            setUpdateTime.invoke(entity,LocalDateTime.now());
            setUpdateUser.invoke(entity,currentId);

        }
    }

}
