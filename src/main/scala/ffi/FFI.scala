package ffi
import java.lang.foreign.Linker;
import java.lang.foreign.SymbolLookup
import java.lang.foreign.MemorySession
import java.lang.foreign.FunctionDescriptor
import java.lang.foreign.ValueLayout.*;
import java.lang.foreign.MemoryAddress
import java.lang.invoke.MethodHandles
import java.lang.foreign.ValueLayout

object FFI {
    val session = MemorySession.openImplicit();
    private val linker = Linker.nativeLinker();
    private val lookup = SymbolLookup.libraryLookup("lib_seija",session)
    private val app_new_handle = linker.downcallHandle(lookup.lookup("app_new").get(),FunctionDescriptor.of(ADDRESS));
    private val app_start_handle = linker.downcallHandle(lookup.lookup("app_start").get(),FunctionDescriptor.ofVoid(ADDRESS));
    private val app_run_handle = linker.downcallHandle(lookup.lookup("app_run").get(),FunctionDescriptor.ofVoid(ADDRESS));
    private val app_set_fps_handle = linker.downcallHandle(lookup.lookup("app_set_fps").get(),FunctionDescriptor.ofVoid(ADDRESS,JAVA_INT));

    private val winit_new_windowconfig_handle = linker.downcallHandle(lookup.lookup("winit_new_windowconfig").get(),FunctionDescriptor.of(ADDRESS));
    private val winit_add_module_handle = linker.downcallHandle(lookup.lookup("winit_add_module").get(),FunctionDescriptor.ofVoid(ADDRESS,ADDRESS));
    private val core_add_module_handle = linker.downcallHandle(lookup.lookup("core_add_module").get(),FunctionDescriptor.ofVoid(ADDRESS));
    private val app_set_on_start_handle = linker.downcallHandle(lookup.lookup("app_set_on_start").get(),FunctionDescriptor.ofVoid(ADDRESS,ADDRESS));
    private val app_set_on_update_handle = linker.downcallHandle(lookup.lookup("app_set_on_update").get(),FunctionDescriptor.ofVoid(ADDRESS,ADDRESS));
    private val core_spawn_entity_handle = linker.downcallHandle(lookup.lookup("core_spawn_entity").get(),FunctionDescriptor.of(JAVA_INT,ADDRESS));
    private val transform_add_handle = linker.downcallHandle(lookup.lookup("tranrform_add").get(),FunctionDescriptor.of(ADDRESS,ADDRESS,JAVA_INT));
    private val tranrform_debug_log_handle = linker.downcallHandle(lookup.lookup("tranrform_debug_log").get(),FunctionDescriptor.ofVoid(ADDRESS,JAVA_INT));

    def appNew():MemoryAddress = app_new_handle.invokeWithArguments().asInstanceOf[MemoryAddress]

    def appStart(ptr:MemoryAddress) = app_start_handle.invokeWithArguments(ptr)

    def appRun(ptr:MemoryAddress):Unit = app_run_handle.invokeWithArguments(ptr)

    def appSetFPS(ptr:MemoryAddress,fps:Int):Unit = app_set_fps_handle.invokeWithArguments(ptr,fps)

    def winitNewConfig():MemoryAddress = winit_new_windowconfig_handle.invokeWithArguments().asInstanceOf[MemoryAddress]

    def winitAddModule(ptr:MemoryAddress,config_ptr:MemoryAddress) = winit_add_module_handle.invokeWithArguments(ptr,config_ptr);
    
    def coreAddModule(ptr:MemoryAddress) = core_add_module_handle.invokeWithArguments(ptr);

    def appSetOnStart(appPtr:MemoryAddress,refc:Class[?],name:String) = {
        val funcDesc = FunctionDescriptor.ofVoid(ADDRESS);
        val func_handle = MethodHandles.lookup().findStatic(refc,name,Linker.upcallType(funcDesc));
        val funcPtr = linker.upcallStub(func_handle,funcDesc,session);
        app_set_on_start_handle.invokeWithArguments(appPtr,funcPtr);
    }

    def appSetOnUpdate(appPtr:MemoryAddress,refc:Class[?],name:String) = {
        val funcDesc = FunctionDescriptor.ofVoid(ADDRESS);
        val func_handle = MethodHandles.lookup().findStatic(refc,name,Linker.upcallType(funcDesc));
        val funcPtr = linker.upcallStub(func_handle,funcDesc,session);
        app_set_on_update_handle.invokeWithArguments(appPtr,funcPtr);   
    }

    def spawnEntity(worldPtr:MemoryAddress):Int = core_spawn_entity_handle.invokeWithArguments(worldPtr).asInstanceOf[Int]
    
    def transformAdd(worldPtr:MemoryAddress,entity:Int):TransformPtr = {
       val ptr = transform_add_handle.invokeWithArguments(worldPtr,entity).asInstanceOf[MemoryAddress]
      
       TransformPtr(ptr)
    } 

    def transformLog(worldPtr:MemoryAddress,entity:Int) = tranrform_debug_log_handle.invokeWithArguments(worldPtr,entity)
}
