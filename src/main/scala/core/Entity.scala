package core;

import java.lang.foreign.MemoryAddress
import ffi.FFI

class Entity(val id:Int) extends AnyVal {
   def add[T]() = ???;
}

object Entity {
    def spawn(worldPtr:MemoryAddress):Entity = {
        Entity(FFI.spawnEntity(worldPtr))
    }
}
