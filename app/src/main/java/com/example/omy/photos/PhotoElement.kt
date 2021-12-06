package com.example.omy.photos

import pl.aprilapps.easyphotopicker.MediaFile

class PhotoElement {
    var image = -1
    var file: MediaFile? = null

    constructor(image: Int) {
        this.image = image
    }

    constructor(fileX: MediaFile?) {
        file = fileX
    }
}