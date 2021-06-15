package com.ps.omarmattr.qalby.model

import android.os.Parcelable
import com.ps.omarmattr.qalby.R
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Dua(val id: Int, val name: String, val image: Int) : Parcelable

fun getListDua() =
    arrayListOf(
        Dua(1, "Morning", R.drawable.dua_morning),
        Dua(2, "Evening", R.drawable.dua_evening),
        Dua(3, "Protection", R.drawable.dua_protection),
        Dua(4, "Before Sleep", R.drawable.dua_protection_during_the_night),
        Dua(5, "Fright During Sleep", R.drawable.dua_fright_during_sleep),
        Dua(6, "Bad Dream", R.drawable.dua_after_a_bad_dream),
        Dua(7, "Warding off Evil Whisperings", R.drawable.dua_warding_off_evil_whispering),
        Dua(8, "Warding off Evil-eye", R.drawable.dua_warding_off_evil_eye),
        Dua(9, "Protection Against Black Magic", R.drawable.dua_protection_against_black_magic),
        Dua(
            10,
            "Protection Against The Evils of Jinn",
            R.drawable.dua_protection_against_the_evils_of_jinn
        ),
        Dua(11, "Protection from An Enemy", R.drawable.dua_protection_from_enemy),
        Dua(12, "Relief from Sorrow and Distress", R.drawable.dua_relief_from_sorrow_distress),
        Dua(13, "Accepted Supplications", R.drawable.dua_accepted_supplications),
        Dua(14, "Supplications For After Solah", R.drawable.dua_supplications_for_after_solah),
        Dua(
            15,
            "Seeking Divine Counsel Istikharah",
            R.drawable.dua_seeking_divine_counsel_for_istikharah
        ),
        Dua(16, "Supplications For Healing", R.drawable.dua_supplications_for_healing),
        Dua(
            17,
            "Supplications For Visiting The Sick",
            R.drawable.dua_supplications_for_visiting_the_sick
        ),
        Dua(
            18,
            "Seeing Someone in Illness or Trial",
            R.drawable.dua_seeing_someone_in_ilness_of_trial
        ),
        Dua(19, "Receiving Unfortunate News", R.drawable.dua_receiving_unfortunate_news),
        Dua(20, "Supplications For Forgiveness", R.drawable.dua_supplications_for_forgiveness),
        Dua(21, "Forgiveness of The Deceased", R.drawable.dua_forgiveness_of_the_deceased),
    )