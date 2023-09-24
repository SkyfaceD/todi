package org.skyfaced.noti.settings.locale

import androidx.compose.runtime.Composable
import cafe.adriel.lyricist.LocalStrings

val strings: Strings
    @Composable
    get() = LocalStrings.current

@Suppress("PropertyName")
interface Strings {
    val cd_4_pda: String
    val cd_add_note: String
    val cd_amazon: String
    val cd_back: String
    val cd_delete: String
    val cd_github: String
    val cd_google_play: String
    val cd_huawei: String
    val cd_settings: String

    val msg_flow_exception: String
    val msg_no_notes: String
    val msg_note_deleted: String
    val msg_note_not_deleted: String
    val msg_note_not_found: String
    val msg_note_not_inserted: String
    val msg_select_one_item: String
    val msg_unexpected_exception: String

    val placeholder_app_version: (version: String) -> String

    val lbl_about_app: String
    val lbl_add: String
    val lbl_add_note: String
    val lbl_amoled_support: String
    val lbl_amoled_support_summary: String
    val lbl_cancel: String
    val lbl_column_number: String
    val lbl_creating: String
    val lbl_description: String
    val lbl_dynamic_theme: String
    val lbl_dynamic_theme_summary: String
    val lbl_editing: String
    val lbl_external_links: String
    val lbl_language: String
    val lbl_language_english: String
    val lbl_language_english_native: String
    val lbl_language_russian: String
    val lbl_language_russian_native: String
    val lbl_ok: String
    val lbl_save: String
    val lbl_select_language: String
    val lbl_select_theme: String
    val lbl_settings: String
    val lbl_system_theme: String
    val lbl_theme: String
    val lbl_theme_dark: String
    val lbl_theme_light: String
    val lbl_title: String
    val lbl_try_again: String
    val lbl_undo: String
    val lbl_view_grid: String
    val lbl_view_linear: String
    val lbl_view_select_list_type: String
    val lbl_view_type: String
}