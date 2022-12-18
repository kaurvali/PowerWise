package ee.ut.cs.powerwise

import androidx.compose.runtime.compositionLocalOf
import androidx.fragment.app.FragmentManager


val LocalFragmentManagerProvider = compositionLocalOf<FragmentManager?> { null }