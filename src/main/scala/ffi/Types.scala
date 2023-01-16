package ffi

import java.lang.foreign.MemoryAddress
import java.lang.foreign.ValueLayout

class Vec3(val ptr:MemoryAddress) extends AnyVal {
    
    def getX():Float = ptr.get(ValueLayout.JAVA_FLOAT,0)

    def setX(value:Float) = ptr.set(ValueLayout.JAVA_FLOAT,0,value);
}

class Quat(val ptr:MemoryAddress) extends AnyVal {
    def setX(value:Float) = ptr.set(ValueLayout.JAVA_FLOAT,0,value);
}

class TransformPtr(val ptr:MemoryAddress) extends AnyVal {
    def scale():Vec3 = Vec3(ptr)

    def rotation():Quat = Quat(ptr.addOffset(16))
    
    def position():Vec3 = Vec3(ptr.addOffset(32))
}