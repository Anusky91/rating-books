package es.anusky.rating_books.cqrs.infrastructure.query;

import es.anusky.rating_books.cqrs.application.query.Query;
import es.anusky.rating_books.cqrs.application.query.QueryBus;
import es.anusky.rating_books.cqrs.application.query.QueryHandler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Component
public class SimpleQueryBus implements QueryBus, ApplicationContextAware {

    private final Map<Class<? extends Query<?>>, QueryHandler<?, ?>> handlers = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, QueryHandler> beans = applicationContext.getBeansOfType(QueryHandler.class);
        for (QueryHandler<?, ?> handler : beans.values()) {
            Class<?> handlerClass = handler.getClass();
            Class<?> queryType = resolveQueryType(handlerClass);
            if (queryType != null) {
                handlers.put((Class<? extends Query<?>>) queryType, handler);
            }
        }
    }

    private Class<?> resolveQueryType(Class<?> handlerClass) {
        for (Type iface : handlerClass.getGenericInterfaces()) {
            if (iface instanceof ParameterizedType parameterized &&
                    parameterized.getRawType().equals(QueryHandler.class)) {

                Type typeArg = parameterized.getActualTypeArguments()[0];
                if (typeArg instanceof Class<?> clazz) {
                    return clazz;
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R, Q extends Query<R>> R ask(Q query) {
        QueryHandler<Q, R> handler = (QueryHandler<Q, R>) handlers.get(query.getClass());

        if (handler == null) {
            throw new IllegalArgumentException("No handler registered for query: " + query.getClass().getName());
        }

        return handler.handle(query);
    }
}
