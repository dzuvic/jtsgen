package dz.jtsgen.processor.renderer;

import dz.jtsgen.processor.model.TypeScriptModel;
import dz.jtsgen.processor.renderer.model.TypeScriptRenderModel;
import dz.jtsgen.processor.renderer.module.ModuleGenerator;
import dz.jtsgen.processor.renderer.module.TSModule;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static dz.jtsgen.processor.renderer.query.PackageQuery.topPackages;
import static dz.jtsgen.processor.util.StringUtils.dotToUpperCamelCase;
import static dz.jtsgen.processor.util.StringUtils.isPackageFriendly;
import static java.util.logging.Level.INFO;


public class TSRenderer {

    public static final String JTSGEN_OUTPUT_OPTION = "jtsgenModuleName";

    private static Logger LOG = Logger.getLogger(TSRenderer.class.getName());

    private final ProcessingEnvironment env;
    private final TypeScriptRenderModel model;
    private final ModuleGenerator moduleGenerator;

    public TSRenderer(ProcessingEnvironment env, TypeScriptModel model) {
        this.model = new TypeScriptRenderModel(model);
        this.env = env;
        this.moduleGenerator = new ModuleGenerator(this.model, env);
    }

    /**
     * @return the module name of all type script types, that are not bound to any package module
     */
    public Optional<String> mainModuleName() {
        if (env.getOptions().get(JTSGEN_OUTPUT_OPTION) != null) {
            String moduleName = env.getOptions().get(JTSGEN_OUTPUT_OPTION);
            if (isPackageFriendly(moduleName)) return Optional.of(env.getOptions().get(JTSGEN_OUTPUT_OPTION));
            env.getMessager().printMessage(Diagnostic.Kind.ERROR,"the module name for the default module specified using the jtsgenModuleName option is not a valid package name. Unfortunately the java compiler need a valid package name when creating a ressource");
            return Optional.empty();
        }
        List<String> candidates = topPackages(model);
        LOG.finest(() -> "TSR candidates: " + (candidates==null?"null": candidates.toString()+ ", size:"+candidates.size()));
        List<String> commonOrTop = commonOrTop(candidates);
        LOG.finest(() -> "TSR common: " + (commonOrTop==null?"null": commonOrTop.toString() + ", size:"+commonOrTop.size()));
        if (commonOrTop == null || commonOrTop.size() != 1) {
            env.getMessager().printMessage(Diagnostic.Kind.ERROR,"module name could not be determined automatically. Specify the name for the default module using (-ajtsgenModuleName) or create common super package");
            return Optional.empty();
        }
        final String moduleName = commonOrTop.iterator().next();
        if ("".equals(moduleName)) {
            env.getMessager().printMessage(Diagnostic.Kind.WARNING,"determined module name is empty, using 'UnknownModule' as module name. Default module name can be set using (-ajtsgenModuleName) or using a common super package");
            return Optional.of("Unknown");
        }
        return Optional.of(dotToUpperCamelCase(moduleName));

    }

    private List<String> commonOrTop(List<String> candidates) {
        return candidates;
    }


    public void writeFiles() {
        Optional<String> mainModuleName = mainModuleName();
        if (mainModuleName.isPresent()) {
            LOG.log(INFO, () -> String.format("Main Module Name %s", mainModuleName.get()));
            TSModule module = new TSModule(mainModuleName.get(), model.getTsTypes());
            moduleGenerator.writeModule(module);
        } else {
            env.getMessager().printMessage(Diagnostic.Kind.ERROR, "specify a default module name (-ajtsgenModuleNmae) or create common super package");
        }

    }
}
