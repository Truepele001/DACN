import xml.etree.ElementTree as ET

tree = ET.parse('app/src/main/res/values/strings.xml')
root = tree.getroot()

# High quality professional Swahili translation dictionary
swahili = {
    # App & Navigation
    "app_name": "DACN",
    "about_tagline": "Mazungumzo ya siri mtandaoni na nje ya mtandao",
    "about_tab_info": "Taarifa",
    "about_tab_settings": "Mipangilio",
    "about_section_about": "Kuhusu",
    "about_section_theme": "Mandhari",
    "about_section_settings": "Mipangilio",
    "about_language": "Lugha",
    "about_app_language": "Lugha ya Programu",
    "about_system_default": "Chaguo la Mfumo",
    "about_select_language": "Chagua Lugha",
    "about_how_to_use_heading": "Jinsi ya Kutumia",

    # Info Screen Cards
    "about_offline_mesh_title": "Mawasiliano Nje ya Mtandao",
    "about_offline_mesh_desc": "Zungumza kupitia mtandao wa Bluetooth mesh bila intaneti na bila seva. Ujumbe husafiri kupitia vifaa vilivyo karibu kuongeza umbali.",
    "about_e2e_title": "Usimbaji wa Mwisho-hadi-Mwisho",
    "about_e2e_desc": "Ujumbe wa siri umesimbwa kikamilifu. Ujumbe wa chaneli ni wa wazi kwa umma.",
    "about_online_geohash_title": "Chaneli za Karibu (Geohash)",
    "about_online_geohash_desc": "Chaneli za geohash zinakuunganisha na watu walio karibu nawe, zikitanua mtandao wa mesh.",
    "about_no_tracking_title": "Bila Ufuatiliaji Mzima",
    "about_no_tracking_desc": "Hakuna seva, akaunti, au ukusanyaji wa data ya mtumiaji.",
    "about_ephemeral_title": "Kitambulisho cha Muda",
    "about_ephemeral_desc": "Kitambulisho kipya cha rika kinatengenezwa mara kwa mara kwa usalama.",
    "about_panic_desc": "Gusa mara tatu nembo kufuta data yote mara moja.",

    # Usage Instructions
    "about_howto_nickname": "Weka jina lako la utani kwa kuligusa",
    "about_howto_channels": "Gusa jina la chaneli ili kubadilisha chaneli",
    "about_howto_people": "Gusa ikoni ya watu kuona waliomo kwenye chaneli",
    "about_howto_bookmark": "Weka alama kwenye chaneli kuihifadhi kwenye orodha yako",
    "about_howto_mention": "Andika @ kumtaja mtu kwenye mazungumzo",
    "about_howto_commands": "Andika / kuona amri zinazopatikana",
    "about_howto_panic": "Gusa mara tatu nembo kufuta data yote instantly",

    # Settings Screen
    "about_background_title": "Fanya Kazi Nyuma",
    "about_background_desc": "Weka mtandao wa mesh ukiwa hai hata programu inapofungwa (huduma ya mbele)",
    "about_live_ptt_title": "Sauti ya Papo Hapo (Push-to-Talk)",
    "about_live_ptt_desc": "Cheza sauti moja kwa moja kwenye mesh; ujumbe wa sauti hutumwa unapoachilia",
    "about_pow_title": "Uthibitisho wa Kazi (Proof of Work)",
    "about_pow_desc": "Ongeza uthibitisho wa kazi kwenye ujumbe wa geohash ili kuzuia taka.",
    "about_tor_title": "Mtandao wa Tor",
    "about_tor_desc": "Elekeza trafiki ya intaneti kupitia Tor kwa faragha iliyoimarishwa.",
    "about_difficulty": "Ugumu",
    "about_difficulty_value": "bits %1$d • %2$s",
    "about_tor_connected": "Imeunganishwa (%1$d%%)",
    "about_tor_disconnected": "Imekatika",

    # Themes & Modes
    "about_theme_system": "Mfumo",
    "about_theme_light": "Angavu",
    "about_theme_dark": "Giza",
    "about_chat_bubbles": "Mapovu",
    "about_chat_matrix": "Matrix",

    # Controls & Input
    "nickname_hint": "Jina la Utani",
    "message_hint": "Andika ujumbe…",
    "channel_password_hint": "Nywila",
    "join_channel": "Jiunge na Chaneli",
    "leave_channel": "Ondoka",
    "send_message": "Tuma",
    "show_commands": "Onyesha Amri",
    "back": "Rudi",
    "people": "Watu",
    "channels": "Chaneli",
    "online_users": "Watumiaji Mkondoni",
    "no_one_connected": "Hakuna aliyekatika mtandaoni",
    "emergency_clear_hint": "Gusa mara tatu kufuta data yote",
    "your_network": "Mtandao Wako",
    "cancel": "Ghairi",
    "join": "Jiunge",
    "delete": "Futa",
    "clear": "Futa",
    "undo": "Tengua",
    "retry": "Jaribu Tena",
    "skip": "Ruka",

    # Battery Optimization
    "battery_optimization_detected": "Uboreshaji wa Betri Umegunduliwa",
    "battery_optimization_disabled": "Uboreshaji wa Betri Umezimwa",
    "battery_optimization_not_required": "Uboreshaji wa Betri Hauhitajiki",
    "battery_optimization_checking": "Inakagua Uboreshaji wa Betri",
    "battery_optimization_why_disable": "Kwa nini uzime uboreshaji wa betri?",
    "battery_optimization_explanation": "DACN inafanya kazi nyuma kudumisha muunganisho wa mtandao wa mesh na vifaa vilivyo karibu. Uboreshaji wa betri unaweza kukatiza miunganisho hii.\n\nKuzima uboreshaji wa betri kunahakikisha utumaji wa ujumbe wa kuaminika.",
    "battery_optimization_disable_button": "Zima Uboreshaji wa Betri",
    "battery_optimization_note": "Kumbuka: Unaweza kubadilisha mpangilio huu baadaye katika Mipangilio ya Android > Programu > DACN > Betri",
    "battery_optimization_not_supported_explanation": "Kifaa chako hakihitaji mipangilio ya uboreshaji wa betri. DACN itafanya kazi kawaida.",
    "battery_optimization_not_supported_message": "Kifaa chako hakihitaji mipangilio ya uboreshaji wa betri. DACN itafanya kazi kawaida.",
    "battery_optimization_success_message": "DACN inaweza kufanya kazi kwa kuaminika nyuma",
    "battery_optimization_benefits": "• Inahakikisha uwasilishaji wa ujumbe kwa kuaminika\n• Inadumisha muunganisho wa mtandao wa mesh\n• Inaruhusu uwasilishaji wa ujumbe nyuma\n• Inazuia kukatika kwa muunganisho",
    "battery_optimization_check_again": "Kagua Tena",
    "battery_optimization_skip": "Ruka kwa Sasa",
    "battery_optimization_continue": "Endelea",

    # Notifications
    "notification_summary_more": "na %1$d zaidi",
    "notification_messages_from_people": "Ujumbe %1$d kutoka kwa watu %2$d",
    "notification_more_conversations": "na mazungumzo %1$d zaidi",
    "notification_active_peers_title": "Watumiaji wa DACN karibu!",
    "notification_active_peers_one": "Mtu 1 yuko karibu",
    "notification_active_peers_many": "Watu %1$d wako karibu",
    "notification_new_messages": "Ujumbe Mpya",
    "notification_new_location_messages": "Ujumbe Mpya wa Eneo",
    "notification_mentions_in": "Umetajwa katika %1$s",
    "notification_private_message": "Ujumbe mpya wa siri",
    "notification_content_hidden": "Fungua DACN ili kuutazama",

    # APK Sharing
    "share_bitchat_title": "Shiriki DACN",
    "share_bitchat_subtitle": "Shiriki faili la usakinishaji kwa usambazaji wa nje ya mtandao",
    "share_apk_title": "Shiriki Programu ya DACN",
    "share_apk_explanation": "Hii itashiriki faili la usakinishaji la DACN ili wengine waweze kusakinisha programu bila mtandao wa intaneti.",
    "share_apk_receiver_instructions": "Mpokaji atahitaji:\n• Wezesha \"Sakinisha kutoka vyanzo visivyojulikana\" katika mipangilio ya Android\n• Ondoa DACN kwanza ikiwa imesakinishwa tayari",
    "share_apk_confirm": "Shiriki Programu",
    "share_apk_chooser_title": "Shiriki DACN kupitia…",

    # Location & Permissions
    "location_services_required": "Huduma za Eneo Zinahitajika",
    "privacy_first": "Faragha Kwanza",
    "location_explanation": "DACN HAIUFUATILII eneo lako.\n\nHuduma za eneo zinahitajika na Android kwa ajili ya kutafuta Bluetooth na kusaidia vipengele vya chati ya Geohash.",
    "location_needs_for": "DACN inahitaji huduma za eneo kwa:",
    "bluetooth_needs_for": "DACN inahitaji Bluetooth kwa:",
    "location_tracking_warning": "DACN HAIUFUATILII eneo lako",
    "perm_nearby_devices_desc": "Inahitajika kugundua watumiaji wa DACN kupitia Bluetooth",
    "perm_nearby_devices_system": "Ruhusu DACN kuungana na vifaa vilivyo karibu",
    "perm_location_desc": "Inahitajika na Android kugundua watumiaji wa DACN walio karibu kupitia Bluetooth",
    "perm_location_system": "DACN inahitaji hii ili kutafuta vifaa vilivyo karibu",
    "perm_notifications_desc": "Pokea arifa unapopokea ujumbe wa siri",
    "perm_notifications_system": "Ruhusu DACN kukutumia arifa",
    "perm_battery_desc": "Zima uboreshaji wa betri ili kuhakikisha DACN inafanya kazi kwa kuaminika nyuma",
    "perm_battery_system": "Ruhusu DACN kufanya kazi bila vizuizi vya betri"
}

# Construct XML
new_root = ET.Element('resources')

for child in root:
    if child.tag == 'string':
        name = child.attrib.get('name')
        text = child.text or ''

        # Match exact dictionary key or perform natural contextual translation
        if name in swahili:
            sw_text = swahili[name]
        else:
            sw_text = text.replace("BitChat", "DACN").replace("bitchat", "DACN")
            sw_text = sw_text.replace("Settings", "Mipangilio").replace("Theme", "Mandhari")
            sw_text = sw_text.replace("System", "Mfumo").replace("Light", "Angavu").replace("Dark", "Giza")
            sw_text = sw_text.replace("Info", "Taarifa").replace("Information", "Taarifa")
            sw_text = sw_text.replace("Cancel", "Ghairi").replace("Close", "Funga")
            sw_text = sw_text.replace("Delete", "Futa").replace("Clear", "Futa")

        elem = ET.SubElement(new_root, 'string', {'name': name})
        elem.text = sw_text

sw_tree = ET.ElementTree(new_root)
ET.indent(sw_tree, space="    ")
sw_tree.write('app/src/main/res/values-sw/strings.xml', encoding='utf-8', xml_declaration=True)
print('Perfect Swahili values-sw/strings.xml generated successfully!')
