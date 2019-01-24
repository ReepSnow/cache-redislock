package com.snow.cacheredislock.api.impl;

import com.snow.cacheredislock.annotation.CacheLock;
import com.snow.cacheredislock.annotation.CacheParam;
import com.snow.cacheredislock.api.CacheKeyGenerator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 上一章说过通过接口注入的方式去写不同的生成规则;
 * @author 王鹏涛
 * @since 2019年1月23日
 */
public class LockKeyGenerator implements CacheKeyGenerator {

    @Override
    public String getLockKey(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        CacheLock lockAnnotation = method.getAnnotation(CacheLock.class);
        /**
         * pjp.getArgs()获取参数时其实都是最终都是去ReflectiveMethodInvocation这个对象里面拿到切点的方法名，方法入参
         * 因为切点的类bookControl首先会生成一个动态代理，同时拿到切点处的方法入参，然后赋值到ReflectiveMethodInvocation这个类，
         * ReflectiveMethodInvocation实现了ProxyMethodInvocation接口，同时ProxyMethodInvocation又是MethodInvocationProceedingJoinPoint的成员变量，
         * MethodInvocationProceedingJoinPoint又是ProceedingJoinPoint的实现类
         */
        final Object[] args = pjp.getArgs();
        final Parameter[] parameters = method.getParameters();
        StringBuilder builder = new StringBuilder();
        /**
         * a,通过pjp.getArgs()获取入参参数值args，至于pjp.getArgs()的获取上面注释有解释
         * b,
         *  1，根据切点获取切点方法，然后获取切点方法的参数列表，
         *  2，然后根据CacheParam这个注解的name字段的赋值（token），来获取需要对那些入参字段进行重复校验
         *  3，通过遍历（1步骤中的）参数名称列表，拿到参数名称与（2步骤中的）name属性的值来判断是否相当，然后记录此时的位置i
         * c 通过b步骤获取的位置值i，然后直接通过a步骤的值args，得到需要的值args[i]，从而就获取了需要拿到的前端参数token
         */
        // TODO 默认解析方法里面带 CacheParam 注解的属性,如果没有尝试着解析实体对象中的
        for (int i = 0; i < parameters.length; i++) {
            final CacheParam annotation = parameters[i].getAnnotation(CacheParam.class);
            if (annotation == null) {
                continue;
            }
            builder.append(lockAnnotation.delimiter()).append(args[i]);
        }
        if (StringUtils.isEmpty(builder.toString())) {
            final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            for (int i = 0; i < parameterAnnotations.length; i++) {
                final Object object = args[i];
                final Field[] fields = object.getClass().getDeclaredFields();
                for (Field field : fields) {
                    final CacheParam annotation = field.getAnnotation(CacheParam.class);
                    if (annotation == null) {
                        continue;
                    }
                    field.setAccessible(true);
                    builder.append(lockAnnotation.delimiter()).append(ReflectionUtils.getField(field, object));
                }
            }
        }
        return lockAnnotation.prefix() + builder.toString();
    }
}