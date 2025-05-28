package es.anusky.rating_books.cqrs.infrastructure.command;

import es.anusky.rating_books.cqrs.application.command.Command;
import es.anusky.rating_books.cqrs.application.command.CommandBus;
import es.anusky.rating_books.cqrs.application.command.CommandHandler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Component
public class SimpleCommandBus implements CommandBus, ApplicationContextAware {

    private final Map<Class<? extends Command>, CommandHandler<?>> handlers = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, CommandHandler> beans = applicationContext.getBeansOfType(CommandHandler.class);
        for (CommandHandler<?> handler : beans.values()) {
            Class<?> handlerClass = handler.getClass();
            Class<?> commandType = resolveCommandType(handlerClass);
            if (commandType != null) {
                handlers.put((Class<? extends Command>) commandType, handler);
            }
        }
    }

    private Class<?> resolveCommandType(Class<?> handlerClass) {
        for (Type iface : handlerClass.getGenericInterfaces()) {
            if (iface instanceof ParameterizedType parameterized && parameterized.getRawType().equals(CommandHandler.class)) {
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
    public <C extends Command> void dispatch(C command) {
        CommandHandler<C> handler = (CommandHandler<C>) handlers.get(command.getClass());

        if (handler == null) {
            throw new IllegalArgumentException("No handler registered for command: " + command.getClass().getName());
        }

        handler.handle(command);
    }
}


