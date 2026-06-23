package com.kqlite.demo.contacts.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kqlite.demo.contacts.model.ContactType

@Composable
fun ContactTypeChip(type: ContactType) {
    val color = when (type) {
        ContactType.Family -> Color(0xFF81C784)
        ContactType.Friend -> Color(0xFF64B5F6)
        ContactType.Work -> Color(0xFFE57373)
        ContactType.Other -> Color.Gray
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = type.name,
            color = color,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview
@Composable
fun ContactTypeChipFamilyPreview() {
    MaterialTheme {
        ContactTypeChip(ContactType.Family)
    }
}

@Preview
@Composable
fun ContactTypeChipFriendPreview() {
    MaterialTheme {
        ContactTypeChip(ContactType.Friend)
    }
}

@Preview
@Composable
fun ContactTypeChipWorkPreview() {
    MaterialTheme {
        ContactTypeChip(ContactType.Work)
    }
}

@Preview
@Composable
fun ContactTypeChipOtherPreview() {
    MaterialTheme {
        ContactTypeChip(ContactType.Other)
    }
}