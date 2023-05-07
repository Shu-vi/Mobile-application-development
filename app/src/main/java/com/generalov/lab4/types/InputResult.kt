package com.generalov.lab4.types

enum class InputResult {
    FieldEmpty,//Пустое поле
    FieldIncorrect,//некорректное заполнение. Например в место телефона введены буквы
    FieldDoNotMatch,//если два поля не совпадают(пока применимо только для пароля и его подтверждения)
    FieldShort,//Поле короткое
    FieldLong,//Поле длинное
    Success,//Валидация пройдена
    Initial//начальное состояние
}