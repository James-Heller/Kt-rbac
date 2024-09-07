package space.jamestang.core.util



data class Resp(
    var code: Int? = null,
    var data: Any? = null,
    var msg: String? = null
) {


    companion object {

        @JvmStatic
        fun success() = Resp().apply {
            this.code = 200
            this.data = null
            this.msg = "success"
        }


        @JvmStatic
        fun data(data: Any) = Resp().apply {
            this.code = 200
            this.data = data
            this.msg = "success"
        }

        @JvmStatic
        fun error(msg: String) = Resp().apply {
            this.code = 500
            this.data = null
            this.msg = msg
        }

        @JvmStatic
        fun error(code: Int, msg: String) = Resp().apply {
            this.code = code
            this.data = null
            this.msg = msg
        }
    }
}