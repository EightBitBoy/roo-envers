package de.eightbitboy.roo.envers;

import static org.springframework.roo.model.SpringJavaType.REQUEST_MAPPING;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.addon.plural.PluralMetadata;
import org.springframework.roo.classpath.PhysicalTypeCategory;
import org.springframework.roo.classpath.PhysicalTypeIdentifier;
import org.springframework.roo.classpath.TypeLocationService;
import org.springframework.roo.classpath.TypeManagementService;
import org.springframework.roo.classpath.details.FieldMetadata;
import org.springframework.roo.classpath.details.FieldMetadataBuilder;
import org.springframework.roo.classpath.details.MemberFindingUtils;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetailsBuilder;
import org.springframework.roo.classpath.details.annotations.AnnotationAttributeValue;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.classpath.details.annotations.ArrayAttributeValue;
import org.springframework.roo.classpath.details.annotations.StringAttributeValue;
import org.springframework.roo.classpath.operations.AbstractOperations;
import org.springframework.roo.metadata.MetadataService;
import org.springframework.roo.model.JavaPackage;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.LogicalPath;
import org.springframework.roo.project.Path;
import org.springframework.roo.project.PathResolver;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.project.Dependency;
import org.springframework.roo.project.DependencyScope;
import org.springframework.roo.project.DependencyType;
import org.springframework.roo.support.logging.HandlerUtils;
import org.springframework.roo.support.util.XmlUtils;
import org.w3c.dom.Element;

/**
 * Implementation of operations this add-on offers.
 *
 * @since 1.1
 */
@Component // Use these Apache Felix annotations to register your commands class in the Roo container
@Service
public class EnversOperationsImpl extends AbstractOperations implements EnversOperations {
	private static final JavaSymbolName VALUE = new JavaSymbolName("value");
	
	private static Logger LOG = HandlerUtils.getLogger(EnversCommands.class);
    
    /**
     * Use ProjectOperations to install new dependencies, plugins, properties, etc into the project configuration
     */
    @Reference private ProjectOperations projectOperations;
    
    /**
     * Use PathResolver to get paths for copying files
     */
    @Reference private PathResolver pathResolver;

    /**
     * Use TypeLocationService to find types which are annotated with a given annotation in the project
     */
    @Reference private TypeLocationService typeLocationService;
    
    /**
     * Use TypeManagementService to change types
     */
    @Reference private TypeManagementService typeManagementService;
    
    @Reference private MetadataService metadataService;

    /** {@inheritDoc} */
    public boolean isCommandAvailable() {
        // Check if a project has been created
        return projectOperations.isFocusedProjectAvailable();
    }

    /** {@inheritDoc} */
    public void annotateType(final JavaType type, final JavaPackage javaPackage) {
    	//TODO check for existing controller
    	
        // Use Roo's Assert type for null checks
        Validate.notNull(type, "Java type required");

        // Obtain ClassOrInterfaceTypeDetails for this java type
        ClassOrInterfaceTypeDetails entityDetails = typeLocationService.getTypeDetails(type);

        // Test if the annotation already exists on the target type
        if (entityDetails != null && MemberFindingUtils.getAnnotationOfType(entityDetails.getAnnotations(), new JavaType(RooEnvers.class.getName())) == null) {
            ClassOrInterfaceTypeDetailsBuilder classOrInterfaceTypeDetailsBuilder = new ClassOrInterfaceTypeDetailsBuilder(entityDetails);
            
            // Create JavaType instance for the add-ons trigger annotation
            JavaType rooRooEnvers = new JavaType(RooEnvers.class.getName());

            // Create Annotation metadata
            AnnotationMetadataBuilder annotationBuilder = new AnnotationMetadataBuilder(rooRooEnvers);
            
            // Add annotation to target type
            classOrInterfaceTypeDetailsBuilder.addAnnotation(annotationBuilder.build());
            
            // Save changes to disk
            typeManagementService.createOrUpdateTypeOnDisk(classOrInterfaceTypeDetailsBuilder.build());
        }

        //TODO see ControllerCommands.java in org.springframework.roo.addon.web.mvc.controller
        
        // Create new controller which holds envers-specific mappings
        
        //TODO probably do not need this
        final PluralMetadata pluralMetadata = (PluralMetadata) metadataService.get(
        	PluralMetadata.createIdentifier(entityDetails.getType(),
        	PhysicalTypeIdentifier.getPath(entityDetails.getDeclaredByMetadataId())));
        
        //TODO get controller path by using typeLocationService.getTypePath, maybe other method or class Physicaltypeidentifier,
        //no need for javaPackage parameter then
        final JavaType controller = new JavaType(
        	javaPackage.getFullyQualifiedPackageName() + "." +
        	entityDetails.getType().getSimpleTypeName() + "Controller_Roo_Controller");
        
        /*
        createAutomaticController(controller, entityType, new HashSet<String>(), pluralMetadata.getPlural().toLowerCase());
        */
        
        
        final LogicalPath controllerPath = pathResolver.getFocusedPath(Path.SRC_MAIN_JAVA);
        final String resourceIdentifier = typeLocationService.getPhysicalTypeCanonicalPath(controller, controllerPath);
        LOG.info("resourceIdentifier: " + resourceIdentifier.toString());
        //final ClassOrInterfaceTypeDetails existingController = getExistingController(resourceIdentifier);
        
        //LOG.info(typeLocationService.getTypePath(controller).toString());
        
        
        //LOG.info(existingController.toString());
        
        
        
        final String declaredByMetadataId = PhysicalTypeIdentifier.createIdentifier(controller, pathResolver.getPath(resourceIdentifier));
        LOG.info(declaredByMetadataId.toString());
        
        List<AnnotationMetadataBuilder> annotations = new ArrayList<AnnotationMetadataBuilder>();
        
        // Create annotation @RequestMapping("/myobject/**")
        final List<AnnotationAttributeValue<?>> requestMappingAttributes = new ArrayList<AnnotationAttributeValue<?>>();
        requestMappingAttributes.add(new StringAttributeValue(VALUE, "/" + pluralMetadata.getPlural().toLowerCase()));
        annotations.add(new AnnotationMetadataBuilder(REQUEST_MAPPING, requestMappingAttributes));
        
        ClassOrInterfaceTypeDetailsBuilder cidBuilder = new ClassOrInterfaceTypeDetailsBuilder(
        	declaredByMetadataId, Modifier.PUBLIC, type, PhysicalTypeCategory.ITD);
        
        typeManagementService.createOrUpdateTypeOnDisk(cidBuilder.build());
        
        //
        //
        //
        //TODO just use AnimalController.java
        
        /*
        
        final JavaType exController = new JavaType(
            	javaPackage.getFullyQualifiedPackageName() + "." +
            	entityDetails.getType().getSimpleTypeName() + "Controller");
        

        final LogicalPath exControllerPath = pathResolver.getFocusedPath(Path.SRC_MAIN_JAVA);
        final String exResourceIdentifier = typeLocationService.getPhysicalTypeCanonicalPath(exController, exControllerPath);
        final String exDeclaredByMetadataId = PhysicalTypeIdentifier.createIdentifier(exController, pathResolver.getPath(exResourceIdentifier));
        */
        
        /*
        
        fieldDetails 
        
        FieldMetadataBuilder fieldBuilder = new FieldMetadataBuilder(fieldDetails)
        FieldMetadata foo = 
        
        cidBuilder = new ClassOrInterfaceTypeDetailsBuilder(exDeclaredByMetadataId, Modifier.PUBLIC, exController, PhysicalTypeCategory.CLASS);
        cidBuilder.a
        typeManagementService.createOrUpdateTypeOnDisk(cidBuilder.build());
        */
    }
    
    /** {@inheritDoc} */
    public void setup() {
        // Install the add-on Google code repository needed to get the annotation 
        //projectOperations.addRepository("", new Repository("Envers Roo add-on repository", "Envers Roo add-on repository", "https://roo-envers.googlecode.com/svn/repo"));
        
        List<Dependency> dependencies = new ArrayList<Dependency>();
        
        // Install the dependency on the add-on jar
        //TODO move this to configuration.xml ?
        dependencies.add(new Dependency("de.eightbitboy.roo.envers", "de.eightbitboy.roo.envers", "0.1.0", DependencyType.JAR, DependencyScope.PROVIDED));
        
        // Install dependencies defined in external XML file
        for (Element dependencyElement : XmlUtils.findElements("/configuration/batch/dependencies/dependency", XmlUtils.getConfiguration(getClass()))) {
            dependencies.add(new Dependency(dependencyElement));
        }

        // Add all new dependencies to pom.xml
        projectOperations.addDependencies("", dependencies);
        
        // Modify tags to create views for entity revisions
        this.modifyTags(); //TODO use modified tags for audited entities only
    }
    
    private void modifyTags(){
    	//TODO modify tags instead of overwriting them!!!
    	
    	String targetPath = pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, "/WEB-INF/tags/form/fields");
    	
    	copyDirectoryContents("tags/*.*", targetPath, true);
    }
    
    /**
     * Looks for an existing controller mapped to the given path
     * 
     * Copied directly form org.springframework.roo.addon.web.mvc.controller.ControllerOperationsImpl
     * 
     * @param path (required)
     * @return <code>null</code> if there is no such controller
     */
    private ClassOrInterfaceTypeDetails getExistingController(final String path) {
        for (final ClassOrInterfaceTypeDetails cid : typeLocationService
                .findClassesOrInterfaceDetailsWithAnnotation(REQUEST_MAPPING)) {
            final AnnotationAttributeValue<?> attribute = MemberFindingUtils
                    .getAnnotationOfType(cid.getAnnotations(), REQUEST_MAPPING)
                    .getAttribute(VALUE);
            if (attribute instanceof ArrayAttributeValue) {
                final ArrayAttributeValue<?> mappingAttribute = (ArrayAttributeValue<?>) attribute;
                if (mappingAttribute.getValue().size() > 1) {
                    LOGGER.warning("Skipping controller '"
                            + cid.getName().getFullyQualifiedTypeName()
                            + "' as it contains more than one path");
                    continue;
                }
                else if (mappingAttribute.getValue().size() == 1) {
                    final StringAttributeValue attr = (StringAttributeValue) mappingAttribute
                            .getValue().get(0);
                    final String mapping = attr.getValue();
                    if (StringUtils.isNotBlank(mapping)
                            && mapping.equalsIgnoreCase("/" + path)) {
                        return cid;
                    }
                }
            }
            else if (attribute instanceof StringAttributeValue) {
                final StringAttributeValue mappingAttribute = (StringAttributeValue) attribute;
                if (mappingAttribute != null) {
                    final String mapping = mappingAttribute.getValue();
                    if (StringUtils.isNotBlank(mapping)
                            && mapping.equalsIgnoreCase("/" + path)) {
                        return cid;
                    }
                }
            }
        }
        return null;
    }
}