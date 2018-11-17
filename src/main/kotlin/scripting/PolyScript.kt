package scripting

import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

open class PolyScript(engine: String) {
    val engine: ScriptEngine by lazy { ScriptEngineManager().getEngineByName(engine) }
}

object Python: PolyScript("python")
object Ruby: PolyScript("jruby")
object Lua: PolyScript("luaj")
