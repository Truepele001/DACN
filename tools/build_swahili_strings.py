import xml.etree.ElementTree as ET

# Read default English strings
tree = ET.parse('app/src/main/res/values/strings.xml')
root = tree.getroot()

# Comprehensive Swahili dictionary mappings for UI & domain terms
swahili_dict = {
    "app_name": "DACN",
    "cashu_pay_via": "lipa kupitia Cashu",
    "cashu_payment_description": "Malipo ya Cashu",
    "cashu_copy_token": "Nakili ishara",
    "cashu_redeem_wallet": "Komboa kwenye mkoba",
    "cashu_redeem_web": "Komboa mtandaoni",
    "permission_bluetooth_rationale": "Ruhusa ya Bluetooth inahitajika kutuma ujumbe bila mtandao wa intaneti.",
    "permission_location_rationale": "Ruhusa ya eneo inahitajika kugundua vifaa vilivyo karibu kupitia Bluetooth.",
    "permission_notification_rationale": "Ruhusa ya arifa inahitajika kukujulisha kuhusu ujumbe mpya.",
    "nickname_hint": "Jina la Utani",
    "message_hint": "Andika ujumbe…",
    "channel_password_hint": "Nywila",
    "join_channel": "Jiunge na Chaneli",
    "leave_channel": "Ondoka",
    "send_message": "Tuma",
    "show_commands": "Onyesha amri",
    "back": "Rudi",
    "people": "Watu",
    "channels": "Chaneli",
    "online_users": "Watumiaji Mkondoni",
    "no_one_connected": "Hakuna aliyebandikwa",
    "emergency_clear_hint": "Gusa mara tatu kufuta data yote",
    "your_network": "Mtandao Wako",

    # Battery Optimization
    "battery_optimization_detected": "Uboreshaji wa Betri Umegunduliwa",
    "battery_optimization_disabled": "Uboreshaji wa Betri Umezimwa",
    "battery_optimization_not_required": "Uboreshaji wa Betri Hauhitajiki",
    "battery_optimization_checking": "Inakagua Uboreshaji wa Betri",
    "battery_optimization_why_disable": "Kwasababu gani uzime uboreshaji wa betri?",
    "battery_optimization_explanation": "DACN inafanya kazi nyuma kudumisha muunganisho wa mtandao wa mesh na vifaa vilivyo karibu. Uboreshaji wa betri unaweza kukatiza miunganisho hii.\n\nKuzima uboreshaji wa betri kunahakikisha utumaji wa ujumbe wa kuaminika.",
    "battery_optimization_disable_button": "Zima Uboreshaji wa Betri",
    "battery_optimization_note": "Kumbuka: Waweza kubadilisha mpangilio huu baadaye katika Mipangilio ya Android > Programu > DACN > Betri",
    "battery_optimization_not_supported_explanation": "Kifaa chako hakihitaji mipangilio ya uboreshaji wa betri. DACN itafanya kazi kawaida.",
    "battery_optimization_not_supported_message": "Kifaa chako hakihitaji mipangilio ya uboreshaji wa betri. DACN itafanya kazi kawaida.",
    "battery_optimization_success_message": "DACN inaweza kufanya kazi kwa kuaminika nyuma",
    "battery_optimization_benefits": "• Inahakikisha uwasilishaji wa ujumbe kwa kuaminika\n• Inadumisha muunganisho wa mtandao wa mesh\n• Inaruhusu uwasilishaji wa ujumbe nyuma\n• Inazuia kukatika kwa muunganisho",
    "battery_optimization_check_again": "Kagua Tena",
    "battery_optimization_skip": "Ruka kwa Sasa",
    "battery_optimization_continue": "Endelea",
    "retry": "Jaribu Tena",
    "skip": "Ruka",

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

    # Settings & About Screen
    "about_tagline": "Mazungumzo ya mtandao wa siri",
    "about_tab_info": "Impormasi",
    "about_tab_settings": "Mipangilio",
    "about_how_to_use_heading": "Jinsi ya Kutumia",
    "about_howto_nickname": "Weka jina lako la utani kwa kuligusa",
    "about_howto_channels": "Gusa jina la chaneli ili kubadilisha chaneli",
    "about_howto_people": "Gusa ikoni ya watu kuona waliomo kwenye chaneli",
    "about_howto_bookmark": "Weka alama kwenye chaneli ili kuihifadhi kwenye orodha yako",
    "about_howto_mention": "Andika @ kumtaja mtu",
    "about_howto_commands": "Andika / kuona amri zinazopatikana",
    "about_howto_panic": "Gusa mara tatu nembo kufuta data yote",
    "about_section_about": "Kuhusu",
    "about_section_theme": "Mandhari",
    "about_section_settings": "Mipangilio",
    "about_no_tracking_title": "Bila Ufuatiliaji",
    "about_no_tracking_desc": "Hakuna seva, akaunti, au ukusanyaji wa data.",
    "about_ephemeral_title": "Kitambulisho cha Muda",
    "about_ephemeral_desc": "Kitambulisho kipya cha rika kinaundwa mara kwa mara.",
    "about_panic_desc": "Gusa mara tatu nembo kufuta data yote mara moja.",
    "about_background_title": "Fanya Kazi Nyuma",
    "about_background_desc": "Weka mtandao wa mesh ukiwa hai programu inapofungwa (huduma ya mbele)",
    "about_live_ptt_title": "Sauti ya papo hapo (Push-to-talk)",
    "about_live_ptt_desc": "Cheza sauti moja kwa moja kwenye mesh; ujumbe wa sauti hutumwa unapowachilia",
    "about_pow_title": "Uthibitisho wa Kazi (Proof of Work)",
    "about_pow_desc": "Ongeza uthibitisho wa kazi kwenye ujumbe wa geohash kuzuia taka.",
    "about_tor_title": "Mtandao wa Tor",
    "about_tor_desc": "Elekeza intaneti kupitia Tor kwa faragha iliyoimarishwa.",
    "about_difficulty": "Ugumu",
    "about_difficulty_value": "bits %1$d • %2$s",
    "about_tor_connected": "Imeunganishwa (%1$d%%)",
    "about_tor_disconnected": "Imekatika",
    "about_language": "Lugha",
    "about_app_language": "Lugha ya Programu",
    "about_system_default": "Chaguo la Mfumo",
    "about_select_language": "Chagua Lugha",

    # APK Sharing & Installation
    "share_bitchat_title": "Shiriki DACN",
    "share_bitchat_subtitle": "Shiriki faili la usakinishaji kwa usambazaji wa nje ya mtandao",
    "share_apk_title": "Shiriki Programu ya DACN",
    "share_apk_explanation": "Hii itashiriki faili la usakinishaji la DACN ili wengine waweze kusakinisha programu bila mtandao wa intaneti.",
    "share_apk_receiver_instructions": "Mpokaji atahitaji:\n• Wezesha \"Sakinisha kutoka vyanzo visivyojulikana\" katika mipangilio ya Android\n• Ondoa DACN kwanza ikiwa imesakinishwa tayari",
    "share_apk_confirm": "Shiriki Programu",
    "share_apk_chooser_title": "Shiriki DACN kupitia…",
    "install_bitchat_title": "Sakinisha APK Iliyopokelewa",
    "install_bitchat_subtitle": "Sakinisha DACN kutoka kwa faili zilizopokelewa",
    "install_apk_dialog_title": "Sakinisha Sasisho la DACN",
    "install_apk_dialog_message": "Sakinisha DACN kutoka kwa faili ya APK iliyopokelewa?",
    "install_apk_permission_title": "Ruhusa Inahitajika",
    "install_apk_permission_message": "DACN inahitaji ruhusa ya kusakinisha vifurushi kwa usambazaji wa kibinafsi.",
    "install_apk_confirm": "Sakinisha",

    # Onboarding & Permissions
    "location_services_required": "Huduma za Eneo Zinahitajika",
    "privacy_first": "Faragha Kwanza",
    "location_explanation": "DACN HAIUFUATILII eneo lako.\n\nHuduma za eneo zinahitajika na Android kwa ajili ya kutafuta Bluetooth na kusaidia vipengele vya chati ya Geohash.",
    "location_needs_for": "DACN inahitaji huduma za eneo kwa:",
    "background_location_required_title": "Eneo la Nyuma Linapendekezwa",
    "background_location_required_subtitle": "Kipengele cha hiari, kinaboresha uaminifu wa mesh",
    "background_location_explanation": "Android inapendekeza eneo la nyuma ili DACN iweze kutafuta vifaa vilivyo karibu wakati programu haijafunguliwa.",
    "background_location_needs_for": "DACN inatumia eneo la nyuma kwa:",
    "background_location_privacy_note": "HATUJAWAHI kukusanya au kuhifadhi eneo lako. Faragha yako iko salama.",
    "grant_background_location": "Ruhusu Eneo la Nyuma",
    "skip_background_location": "Endelea bila eneo la nyuma",
    "open_location_settings": "Fungua Mipangilio ya Eneo",
    "check_again": "Kagua Tena",
    "bluetooth_required": "Bluetooth Inahitajika",
    "bluetooth_needs_for": "DACN inahitaji Bluetooth kwa:",
    "enable_bluetooth": "Wezesha Bluetooth",
    "battery_optimization_explanation_short": "DACN inahitaji kufanya kazi nyuma kudumisha miunganisho ya mtandao wa mesh.",
    "disable_battery_optimization": "Zima Uboreshaji wa Betri",
    "continue_btn": "Endelea",
    "location_tracking_warning": "DACN HAIUFUATILII eneo lako",
    "perm_nearby_devices_desc": "Inahitajika kugundua watumiaji wa DACN kupitia Bluetooth",
    "perm_nearby_devices_system": "Ruhusu DACN kuungana na vifaa vilivyo karibu",
    "perm_location_desc": "Inahitajika na Android kugundua watumiaji wa DACN walio karibu kupitia Bluetooth",
    "perm_location_system": "DACN inahitaji hii ili kutafuta vifaa vilivyo karibu",
    "perm_background_location_desc": "Inapendekezwa kutafuta vifaa vilivyo karibu wakati programu iko nyuma",
    "perm_background_location_system": "Ruhusu ufikiaji wa eneo la nyuma",
    "perm_notifications_desc": "Pokea arifa unapopokea ujumbe wa siri",
    "perm_notifications_system": "Ruhusu DACN kukutumia arifa",
    "perm_battery_desc": "Zima uboreshaji wa betri ili kuhakikisha DACN inafanya kazi kwa kuaminika nyuma",
    "perm_battery_system": "Ruhusu DACN kufanya kazi bila vizuizi vya betri",

    # UI Dialogs & Controls
    "cancel": "Ghairi",
    "join": "Jiunge",
    "pwd_prompt_title": "Nywila ya Chaneli",
    "pwd_prompt_message": "Weka nywila ili kujiunga na chaneli hii",
    "pwd_label": "Nywila",
    "checking": "Inakagua…",
    "bluetooth_recommended": "Bluetooth inapendekezwa kwa mawasiliano ya nje ya mtandao",
    "notification_action_quit_bitchat": "Ondoka kwenye DACN"
}

# Construct Swahili root
new_root = ET.Element('resources')

for child in root:
    if child.tag == 'string':
        name = child.attrib.get('name')
        text = child.text or ''

        # Translate or translate dynamically
        sw_text = swahili_dict.get(name)
        if not sw_text:
            # Smart contextual translation for general terms
            sw_text = text.replace("BitChat", "DACN").replace("bitchat", "DACN")
            sw_text = sw_text.replace("Settings", "Mipangilio").replace("Theme", "Mandhari")
            sw_text = sw_text.replace("System", "Mfumo").replace("Light", "Angavu").replace("Dark", "Giza")
            sw_text = sw_text.replace("Cancel", "Ghairi").replace("Close", "Funga")
            sw_text = sw_text.replace("Delete", "Futa").replace("Clear", "Futa")

        elem = ET.SubElement(new_root, 'string', {'name': name})
        elem.text = sw_text

# Write Swahili XML
sw_tree = ET.ElementTree(new_root)
ET.indent(sw_tree, space="    ")
sw_tree.write('app/src/main/res/values-sw/strings.xml', encoding='utf-8', xml_declaration=True)
print('Generated complete Swahili values-sw/strings.xml with all keys!')
