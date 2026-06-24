package com.kqlite.demo.contacts.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Face2
import androidx.compose.material.icons.filled.Face3
import androidx.compose.material.icons.filled.Face4
import androidx.compose.material.icons.filled.Face5
import androidx.compose.material.icons.filled.Face6
import androidx.compose.material.icons.filled.FaceRetouchingNatural
import androidx.compose.material.icons.filled.FaceRetouchingOff
import androidx.compose.material.icons.filled.InsertEmoticon
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.SentimentNeutral
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.SentimentSatisfiedAlt
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material.icons.filled.SentimentVerySatisfied
import androidx.compose.material.icons.filled.TagFaces
import androidx.compose.ui.graphics.vector.ImageVector

class StaticIcons {
    fun staticFaces(): List<ImageVector> {
        return listOf(
            Icons.Filled.Face,
            Icons.Filled.Face2,
            Icons.Filled.Face3,
            Icons.Filled.Face4,
            Icons.Filled.Face5,
            Icons.Filled.Face6,
            Icons.Filled.FaceRetouchingNatural,
            Icons.Filled.FaceRetouchingOff,
            Icons.Filled.TagFaces,
            Icons.Filled.EmojiEmotions,
            Icons.Filled.Mood,
            Icons.Filled.InsertEmoticon,
            Icons.Filled.SentimentNeutral,
            Icons.Filled.SentimentSatisfied,
            Icons.Filled.SentimentDissatisfied,
            Icons.Filled.SentimentVerySatisfied,
            Icons.Filled.SentimentVeryDissatisfied,
            Icons.Filled.SentimentSatisfiedAlt,
        )
    }
}