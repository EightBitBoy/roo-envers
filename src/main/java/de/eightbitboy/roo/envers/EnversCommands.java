package de.eightbitboy.roo.envers;

import static org.springframework.roo.shell.OptionContexts.UPDATE;

import java.util.logging.Logger;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.model.JavaPackage;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.shell.CliAvailabilityIndicator;
import org.springframework.roo.shell.CliCommand;
import org.springframework.roo.shell.CliOption;
import org.springframework.roo.shell.CommandMarker;
import org.springframework.roo.support.logging.HandlerUtils;

/**
 * Sample of a command class. The command class is registered by the Roo shell following an
 * automatic classpath scan. You can provide simple user presentation-related logic in this
 * class. You can return any objects from each method, or use the logger directly if you'd
 * like to emit messages of different severity (and therefore different colours on 
 * non-Windows systems).
 * 
 * @since 1.1
 */
@Component // Use these Apache Felix annotations to register your commands class in the Roo container
@Service
public class EnversCommands implements CommandMarker { // All command types must implement the CommandMarker interface
    
    private static Logger LOG = HandlerUtils.getLogger(EnversCommands.class);
	
    /**
     * Get a reference to the EnversOperations from the underlying OSGi container
     */
    @Reference private EnversOperations operations;
    @Reference private ProjectOperations projectOperations;
    
    /**
     * This method is optional. It allows automatic command hiding in situations when the command should not be visible.
     * For example the 'entity' command will not be made available before the user has defined his persistence settings 
     * in the Roo shell or directly in the project.
     * 
     * You can define multiple methods annotated with {@link CliAvailabilityIndicator} if your commands have differing
     * visibility requirements.
     * 
     * @return true (default) if the command should be visible at this stage, false otherwise
     */
    @CliAvailabilityIndicator({ "envers add" })
    public boolean isCommandAvailable() {
        return operations.isCommandAvailable(); //TODO show add command only after setup has been executed
    }
    
    /**
     * Set up dependencies
     */
    @CliCommand(value = "envers setup", help = "Add envers dependencies")
    public void setup() {
        operations.setup();
    }
    
    /**
     * This method registers a command with the Roo shell. It also offers a mandatory command attribute.
     * 
     * @param type 
     */
    @CliCommand(value = "envers add", help = "Make the type auditable")
    public void add(
    		@CliOption(key = "type", mandatory = true, help = "The java type to apply this annotation to") JavaType target,
    		@CliOption(key = "package", mandatory = true, optionContext = UPDATE, help = "The package in which new controllers will be placed, CHOSE THE SAME PACKAGE USED IN web mvc all command") final JavaPackage javaPackage) {
    	
        if (!javaPackage.getFullyQualifiedPackageName().startsWith(
                projectOperations.getTopLevelPackage(
                        projectOperations.getFocusedModuleName())
                        .getFullyQualifiedPackageName())) {
            LOG.warning("Your controller was created outside of the project's top level package and is therefore not included in the preconfigured component scanning. Please adjust your component scanning manually in webmvc-config.xml");
        }
    	
        operations.annotateType(target, javaPackage);
        operations.addViews(target);
    }
}