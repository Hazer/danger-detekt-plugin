package io.vithor.danger.plugins.detekt

import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList

internal fun <R> NodeList.mapElements(mapper: (Element) -> R): List<R> {
    val elements = mutableListOf<R>()
    for (x in 0 until this.length) {
        with(this.item(x) as Element) {
            if (this.nodeType == Node.ELEMENT_NODE) {
                elements.add(mapper(this))
            }
        }
    }
    return elements
}