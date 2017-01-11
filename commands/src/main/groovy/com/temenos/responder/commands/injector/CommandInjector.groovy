package com.temenos.responder.commands.injector;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.temenos.responder.commands.AdditionCommand;
import com.temenos.responder.commands.GETResource;
import com.temenos.responder.commands.VersionInformation;
import com.temenos.responder.commands.transformers.CustomerTransformer;
import com.temenos.responder.flows.CustomerInformationWithTransformer;
import com.temenos.responder.loader.ClasspathScriptLoader;
import com.temenos.responder.loader.ScriptLoader;
import com.temenos.responder.producer.EntityJsonProducer;
import com.temenos.responder.producer.Producer;

/**
 * CommandInjector is a {@link com.google.inject.Module module} responsible for creating
 * {@link com.temenos.responder.commands.Command Command} singletons.
 *
 * @author Douglas Groves
 */
public class CommandInjector extends AbstractModule {

    @Override
    protected void configure() {
        //utility bindings
        bind(ScriptLoader.class).toInstance(new ClasspathScriptLoader("resources"));
        bind(Producer.class).to(EntityJsonProducer.class);

        //command bindings
        bind(AdditionCommand.class).in(Singleton.class);
        bind(CustomerInformationWithTransformer.class).in(Singleton.class);
        bind(GETResource.class).in(Singleton.class);
        bind(CustomerInformationWithTransformer.class).in(Singleton.class);
        bind(VersionInformation.class).in(Singleton.class);
        bind(CustomerTransformer.class).in(Singleton.class);
    }
}
