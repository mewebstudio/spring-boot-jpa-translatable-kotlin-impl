package com.mewebstudio.translatable.dto.response

data class DetailedErrorResponse(
    override val message: String,
    var items: MutableMap<String, String?>
) : ErrorResponse(message)
