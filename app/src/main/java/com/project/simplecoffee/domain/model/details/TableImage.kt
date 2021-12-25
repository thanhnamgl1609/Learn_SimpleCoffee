package com.project.simplecoffee.domain.model.details

sealed class TableImage(val url: String) {
    object REC_4 :
        TableImage("https://firebasestorage.googleapis.com/v0/b/simple-64565.appspot.com/o/Resource%2FTable%2Fsquare_4_chairs.png?alt=media&token=4d95679a-a624-4e34-91f7-e84530cddca4")

    object CIRCLE_4 :
        TableImage("https://firebasestorage.googleapis.com/v0/b/simple-64565.appspot.com/o/Resource%2FTable%2Fcircle_4_chairs.png?alt=media&token=51576365-497e-456a-96f6-967398481563")

    object REC_6 :
        TableImage("https://firebasestorage.googleapis.com/v0/b/simple-64565.appspot.com/o/Resource%2FTable%2Frectangle_6_chairs.png?alt=media&token=abfc55fb-265b-4b42-ae12-a90212b13d9f")

    companion object {
        fun getURL(size: Int, shape: TableShape): TableImage? =
            if (size == 4) {
                when (shape) {
                    is TableShape.Circle -> CIRCLE_4
                    is TableShape.Rectangle -> REC_4
                    else -> null
                }
            } else if (size == 6) {
                if (shape is TableShape.Rectangle)
                    REC_6
                else null
            } else null
    }
}