package de.eightbitboy.roo.envers;

import java.lang.reflect.Modifier;

import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.details.MethodMetadata;
import org.springframework.roo.classpath.details.MethodMetadataBuilder;
import org.springframework.roo.classpath.itd.AbstractItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.classpath.itd.InvocableMemberBodyBuilder;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;

public class EnversControllerMetadata extends AbstractItdTypeDetailsProvidingMetadataItem {
	public EnversControllerMetadata(String identifier, JavaType aspectName, PhysicalTypeMetadata governorPhysicalTypeMetadata) {
    	super(identifier, aspectName, governorPhysicalTypeMetadata);
    	
    	builder.addMethod(getDoSomethingMethod());
    	
    	builder.build();
	}
	
    private MethodMetadata getDoSomethingMethod() {
    	final InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();
    	bodyBuilder.appendFormalLine("System.out.print(\"doing something\");");
    	
    	final MethodMetadataBuilder methodBuilder = new MethodMetadataBuilder(
    		getId(),
    		Modifier.PUBLIC,
    		new JavaSymbolName("doSomething"),
    		JavaType.VOID_PRIMITIVE,
    		bodyBuilder);
    	
    	return methodBuilder.build();
    }
}