package de.eightbitboy.roo.envers;

import java.lang.reflect.Modifier;
import java.util.logging.Logger;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.roo.classpath.PhysicalTypeIdentifierNamingUtils;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.details.MethodMetadata;
import org.springframework.roo.classpath.details.MethodMetadataBuilder;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadata;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.classpath.itd.AbstractItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.classpath.itd.InvocableMemberBodyBuilder;
import org.springframework.roo.metadata.MetadataIdentificationUtils;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.LogicalPath;
import org.springframework.roo.support.logging.HandlerUtils;

/**
 * This type produces metadata for a new ITD. It uses an {@link ItdTypeDetailsBuilder} provided by 
 * {@link AbstractItdTypeDetailsProvidingMetadataItem} to register a field in the ITD and a new method.
 * 
 * @since 1.1.0
 */
public class EnversMetadata extends AbstractItdTypeDetailsProvidingMetadataItem {

	private static Logger LOG = HandlerUtils.getLogger(EnversCommands.class);
	
    private static final String PROVIDES_TYPE_STRING = EnversMetadata.class.getName();
    private static final String PROVIDES_TYPE = MetadataIdentificationUtils.create(PROVIDES_TYPE_STRING);

    public static final String getMetadataIdentiferType() {
        return PROVIDES_TYPE;
    }
    
    public static final String createIdentifier(JavaType javaType, LogicalPath path) {
        return PhysicalTypeIdentifierNamingUtils.createIdentifier(PROVIDES_TYPE_STRING, javaType, path);
    }

    public static final JavaType getJavaType(String metadataIdentificationString) {
        return PhysicalTypeIdentifierNamingUtils.getJavaType(PROVIDES_TYPE_STRING, metadataIdentificationString);
    }

    public static final LogicalPath getPath(String metadataIdentificationString) {
        return PhysicalTypeIdentifierNamingUtils.getPath(PROVIDES_TYPE_STRING, metadataIdentificationString);
    }

    public static boolean isValid(String metadataIdentificationString) {
        return PhysicalTypeIdentifierNamingUtils.isValid(PROVIDES_TYPE_STRING, metadataIdentificationString);
    }
    
    public EnversMetadata(String identifier, JavaType aspectName, PhysicalTypeMetadata governorPhysicalTypeMetadata) {
    	super(identifier, aspectName, governorPhysicalTypeMetadata);
        Validate.isTrue(isValid(identifier), "Metadata identification string '" + identifier + "' does not appear to be a valid");

        //LOG.info(identifier);
        //LOG.info(governorPhysicalTypeMetadata.toString());
        
        // Add Annotations
        builder.addAnnotation(getEntityAuditAnnotation());
        
        // Add Methods
        builder.addMethod(getDoSomethingMethod()); //TODO remove this whem things start looking good
        
        // Create a representation of the desired output ITD
        itdTypeDetails = builder.build();
    }
    
    /**
     * Creates the @Audited annotation for the entity
     * 
     * @return
     */
    private AnnotationMetadata getEntityAuditAnnotation() {
    	JavaType auditedType = new JavaType("org.hibernate.envers.Audited");
    	AnnotationMetadata auditedAnnotation = getTypeAnnotation(auditedType);
    	
    	AnnotationMetadataBuilder annotationBuilder = new AnnotationMetadataBuilder(auditedAnnotation);
    	annotationBuilder.build();
    	
    	return auditedAnnotation;
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
    
    // Typically, no changes are required beyond this point
    
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("identifier", getId());
        builder.append("valid", valid);
        builder.append("aspectName", aspectName);
        builder.append("destinationType", destination);
        builder.append("governor", governorPhysicalTypeMetadata.getId());
        builder.append("itdTypeDetails", itdTypeDetails);
        return builder.toString();
    }
}
