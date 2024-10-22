// Copyright (c) 2024 Magic Tech Ltd

package fit.magic.cv.repcounter

import fit.magic.cv.PoseLandmarkerHelper

class ExerciseRepCounterImpl : ExerciseRepCounter() {

    // State variables to track repetition and progress
    private var isLungeInProgress = false
    private var previousLeftKneeY = 0f
    private var previousRightKneeY = 0f
    private var previousHipY = 0f  // Track the hip position to analyze progress
    private val movementThreshold = 0.1f  // Threshold for detecting significant movement
    private val progressSmoothingFactor = 0.1f  // Factor for smoothing progress

    override fun setResults(resultBundle: PoseLandmarkerHelper.ResultBundle) {
        // Get the first PoseLandmarkerResult
        val landmarks = resultBundle.results[0].landmarks()  // Access as a property

        // Extract necessary body points (hips, knees) based on MediaPipe indices
        val leftKneeY = landmarks[25].get(25).y()
        val rightKneeY = landmarks[26].get(26).y()
        val hipY = (landmarks[23].get(23).y() + landmarks[24].get(24).y()) / 2  // Average of left and right hips

        // Analyze the movement for reps
        analyzeRepetitions(leftKneeY, rightKneeY, hipY)

        // Update progress bar based on the current movement
        updateProgress(hipY)

    }

    // Function to analyze and count exercise repetitions
    private fun analyzeRepetitions(leftKneeY: Float, rightKneeY: Float, hipY: Float) {
        // Check if the user has reached the "down" position (e.g., knees are bent)
        if (!isLungeInProgress && (leftKneeY < previousLeftKneeY - movementThreshold && rightKneeY < previousRightKneeY - movementThreshold)) {
            // Start of the lunge movement
            isLungeInProgress = true
        }

        // Check if the user has returned to the "up" position (e.g., knees straightened)
        if (isLungeInProgress && (leftKneeY > previousLeftKneeY + movementThreshold && rightKneeY > previousRightKneeY + movementThreshold)) {
            // End of the lunge movement, count the rep
            incrementRepCount()  // This updates the rep count in the UI
            isLungeInProgress = false  // Reset for the next rep
        }

        // Update previous positions for next frame analysis
        previousLeftKneeY = leftKneeY
        previousRightKneeY = rightKneeY
    }

    // Function to update the progress of the current movement
    private fun updateProgress(hipY: Float) {
        // Calculate the normalized progress as the user moves (lower hip position indicates more progress)
        var progress = 1 - hipY  // Normalize the progress as the user goes down
        progress = progressSmoothingFactor * progress + (1 - progressSmoothingFactor) * previousHipY  // Apply smoothing

        // Send progress update to the UI (progress should be between 0 and 1, as Float)
        sendProgressUpdate(progress)

        // Store the current hip position for smoothing the next update
        previousHipY = progress
    }
}