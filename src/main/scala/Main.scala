import java.util.Optional;
import java.lang.foreign.Linker;
import ffi.FFI;
import ffi.TransformPtr;
import core.Entity;
import java.lang.foreign.MemoryAddress
import java.lang.foreign.ValueLayout
import java.lang.foreign.MemorySegment
import java.lang.foreign.MemoryLayout
import java.lang.foreign.MemoryLayout.PathElement
import java.lang.foreign.MemorySession
import ffi.TransformPtr
import javax.xml.crypto.dsig.Transform

class TestGame;
object TestGame {
   var trans:Option[TransformPtr] = None;
   def OnStart(worldPtr:MemoryAddress):Unit = {
      println(s"Onstart:${worldPtr.toRawLongValue()}");
      val eid = FFI.spawnEntity(worldPtr);
      val entity = Entity(eid);
      val t = FFI.transformAdd(worldPtr,eid);
      t.position().setX(666.6f);
      t.rotation().setX(111.4f);
      t.scale().setX(999.9f);
      FFI.transformLog(worldPtr,eid);
      this.trans = Some(t);
   }

   def OnStart2(worldPtr:MemoryAddress):Unit = {
      val entity = Entity.spawn(worldPtr);
      entity.add[Transform]();
      
      
   }

   def OnUpdate(worldPtr:MemoryAddress):Unit = {
      if(this.trans.isDefined) {
         val x = this.trans.get.position().getX();
         println(x);
         val eid = FFI.spawnEntity(worldPtr);
         FFI.transformAdd(worldPtr,eid);
         println(eid);
      }
   }
}

@main
def hello: Unit = {
   val appPtr = FFI.appNew();
   FFI.appSetFPS(appPtr,5);
   FFI.coreAddModule(appPtr);
   var configPtr = FFI.winitNewConfig();
   configPtr.set(ValueLayout.JAVA_FLOAT,0,640f);
   configPtr.set(ValueLayout.JAVA_FLOAT,4,480f);

   FFI.winitAddModule(appPtr,configPtr);
   FFI.appSetOnStart(appPtr,classOf[TestGame],"OnStart");
   FFI.appSetOnUpdate(appPtr,classOf[TestGame],"OnUpdate");
   FFI.appStart(appPtr);
   FFI.appRun(appPtr);
   
}