package com.personal.personalsafetyguard.model

class ParentItem {
    var parentTitle: String? = null
    var parentImage: String? = null
    var childItemList: List<ChildItem> = emptyList() // Use emptyList() instead of null
    var isExpandable: Boolean = false

    constructor() {}

    constructor(parentTitle: String?, parentImage: String?, childItemList: List<ChildItem>?, isExpandable: Boolean) {
        this.parentTitle = parentTitle
        this.parentImage = parentImage
        this.childItemList = childItemList ?: emptyList()
        this.isExpandable = isExpandable
    }
}
