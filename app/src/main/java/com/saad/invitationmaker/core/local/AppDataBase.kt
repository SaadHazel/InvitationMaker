package com.saad.invitationmaker.core.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.saad.invitationmaker.features.backgrounds.converter.BackgroundConverter
import com.saad.invitationmaker.features.backgrounds.models.CategoryModel
import com.saad.invitationmaker.features.editor.models.CategoryModelSticker
import com.saad.invitationmaker.features.home.converters.ViewsConverter
import com.saad.invitationmaker.features.home.models.AllCardsDesigns
import com.saad.invitationmaker.features.home.models.LocalCardDetailsModel

@Database(
    entities = [AllCardsDesigns::class, LocalCardDetailsModel::class, CategoryModel::class, CategoryModelSticker::class],
    version = 1
)
@TypeConverters(ViewsConverter::class, BackgroundConverter::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun cardsDao(): CardsDao
    abstract fun backgroundDao(): BackgroundDao
    abstract fun stickerDao(): StickerDao
}