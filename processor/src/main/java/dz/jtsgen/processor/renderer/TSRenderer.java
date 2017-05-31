package dz.jtsgen.processor.renderer;

import dz.jtsgen.processor.model.TypeScriptModel;
import dz.jtsgen.processor.renderer.model.TypeScriptRenderModel;
import dz.jtsgen.processor.renderer.module.ModuleGenerator;

import javax.annotation.processing.ProcessingEnvironment;
import java.util.logging.Logger;


public final class TSRenderer {

    private static Logger LOG = Logger.getLogger(TSRenderer.class.getName());

    private final TypeScriptRenderModel model;
    private final ModuleGenerator moduleGenerator;

    public TSRenderer(ProcessingEnvironment env, TypeScriptModel model) {
        this.model = new TypeScriptRenderModel(model);
        this.moduleGenerator = new ModuleGenerator(this.model, env);
    }


    public void writeFiles() {
        this.moduleGenerator.writeModule();
    }
}
