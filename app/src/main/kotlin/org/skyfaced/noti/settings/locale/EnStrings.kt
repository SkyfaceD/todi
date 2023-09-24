package org.skyfaced.noti.settings.locale

import cafe.adriel.lyricist.LyricistStrings

@LyricistStrings(languageTag = Locales.En, default = true)
val EnStrings = EnStringsClass() as Strings

class EnStringsClass : Strings {
    override val cd_4_pda = "Go to 4PDA page"
    override val cd_add_note = "Go to note creation"
    override val cd_amazon = "Go to Amazon Appstore page"
    override val cd_back = "Go back"
    override val cd_delete = "Delete"
    override val cd_github = "Go to Github page"
    override val cd_google_play = "Go to Google Play page"
    override val cd_huawei = "Go to App Gallery page"
    override val cd_settings = "Go to settings"

    override val msg_flow_exception = "An error occurred while retrieving data"
    override val msg_no_notes = "It looks like you don't have any notes"
    override val msg_note_deleted = "Note successfully deleted"
    override val msg_note_not_deleted = "Failed to delete note"
    override val msg_note_not_found = "Failed to find note"
    override val msg_note_not_inserted = "Failed to create note"
    override val msg_select_one_item = "Please, select at least one item"
    override val msg_unexpected_exception = "Unexpected error"

    override val placeholder_app_version = { version: String ->
        "Version %s".format(version)
    }

    override val lbl_about_app = "About"
    override val lbl_add = "Add"
    override val lbl_add_note = "Add note"
    override val lbl_amoled_support = "AMOLED support"
    override val lbl_amoled_support_summary = "Uses less power on AMOLED screens"
    override val lbl_cancel = "Cancel"
    override val lbl_column_number = "Number of columns"
    override val lbl_creating = "Creating"
    override val lbl_description = "Description"
    override val lbl_dynamic_theme = "Dynamic theme"
    override val lbl_dynamic_theme_summary =
        "Applies a theme created on the color scheme of your wallpaper"
    override val lbl_editing = "Editing"
    override val lbl_external_links = "External links"
    override val lbl_language = "Language"
    override val lbl_language_english = "English"
    override val lbl_language_english_native = "English"
    override val lbl_language_russian = "Russian"
    override val lbl_language_russian_native = "Русский язык"
    override val lbl_ok = "OK"
    override val lbl_save = "Save"
    override val lbl_select_language = "Select language"
    override val lbl_select_theme = "Select theme"
    override val lbl_settings = "Settings"
    override val lbl_system_theme = "Follow system"
    override val lbl_theme = "Theme"
    override val lbl_theme_dark = "Dark"
    override val lbl_theme_light = "Light"
    override val lbl_title = "Title"
    override val lbl_try_again = "Try again"
    override val lbl_undo = "Undo"
    override val lbl_view_grid = "Grid"
    override val lbl_view_linear = "Linear"
    override val lbl_view_select_list_type = "Select list view type"
    override val lbl_view_type = "List type"
}