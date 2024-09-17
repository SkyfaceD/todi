package org.skyfaced.noti.settings.locale

import cafe.adriel.lyricist.LyricistStrings

@LyricistStrings(languageTag = Locales.Ru)
val RuStrings = RuStringsClass() as Strings

class RuStringsClass : Strings {
    override val cd_4_pda = "Перейти на 4PDA страницу"
    override val cd_add_note = "Перейти в создание заметки"
    override val cd_amazon = "Перейти на страницу Amazon Appstore"
    override val cd_back = "Назад"
    override val cd_delete = "Удалить"
    override val cd_github = "Перейти на страницу Github"
    override val cd_google_play = "Перейти на страницу Google Play"
    override val cd_huawei = "Перейти на страницу App Gallery"
    override val cd_settings = "Перейти в настройки"

    override val msg_flow_exception = "Произошла ошибка при попытке получить данные"
    override val msg_no_notes = "Похоже, у Вас нет заметок"
    override val msg_note_deleted = "Заметка успешно удалена"
    override val msg_note_not_deleted = "Не удалось удалить заметку"
    override val msg_note_not_found = "Не удалось найти заметку"
    override val msg_note_not_inserted = "Не удалось создать заметку"
    override val msg_select_one_item = "Пожалуйста, выберите хотя бы один элемент"
    override val msg_unexpected_exception = "Произошла непредвиденная ошибка"

    override val placeholder_app_version = { version: String ->
        "Версия %s".format(version)
    }

    override val lbl_about_app = "О программе"
    override val lbl_add = "Добавить"
    override val lbl_add_note = "Добавить заметку"
    override val lbl_amoled_support = "Поддержка AMOLED"
    override val lbl_amoled_support_summary = "Потребляет меньше заряда батареи на AMOLED экранах"
    override val lbl_cancel = "Отмена"
    override val lbl_column_number = "Количество колонок"
    override val lbl_creating = "Создание"
    override val lbl_description = "Описание"
    override val lbl_dynamic_theme = "Динамическая тема"
    override val lbl_dynamic_theme_summary =
        "Применяет тему приложения, основанную на цветовой палитре обоев устройства"
    override val lbl_editing = "Редактирование"
    override val lbl_external_links = "Внешние ссылки"
    override val lbl_language = "Язык"
    override val lbl_language_english = "Английский язык"
    override val lbl_language_english_native = "English"
    override val lbl_language_russian = "Русский язык"
    override val lbl_language_russian_native = "Русский язык"
    override val lbl_ok = "Ок"
    override val lbl_save = "Сохранить"
    override val lbl_select_language = "Выберите язык"
    override val lbl_select_theme = "Выберите тему"
    override val lbl_settings = "Настройки"
    override val lbl_system_theme = "Как в системе"
    override val lbl_theme = "Тема"
    override val lbl_theme_dark = "Тёмная"
    override val lbl_theme_light = "Светлая"
    override val lbl_title = "Заголовок"
    override val lbl_try_again = "Попробовать снова"
    override val lbl_undo = "Отменить"
    override val lbl_view_grid = "Сеткой"
    override val lbl_view_linear = "Построчно"
    override val lbl_view_select_list_type = "Выберите тип списка"
    override val lbl_view_type = "Тип списка"
}