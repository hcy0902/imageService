# Overview
Hi "Candidates Name Here"

Thank you for your interest in the H-E-B Digital Backend team. The first part of your Engineering interview includes producing some sample code for you to review with an interviewer during the Technical Problem Review interview. 

Before starting your project, a hiring manager or engineer will go over the details of the technical problem, ensure you have proper access to the repository, and answer Q&A giving you the opportunity to get clarification on the problem and ask any questions you may have. After this, you have 5 days to complete the assessment. We will schedule a 1-hour session to review your submission with an interviewer.

Please let us know if you have any questions in the meantime. We look forward to seeing your work!

# Technical Problem Review 
- Expect to spend no more than 4 hours of your own time on this exercise.
- Submit your solution to this repository as a pull request. 
- Be sure to ask questions during the Q&A and the code review. The interviewer is here to help you succeed!
- Make sure to review your code and be able to walk someone through your decisions.
- Be prepared to discuss the 3rd party libraries you use, including their strengths and weakness, implementation, and complexity. 
- Be prepared to demo your solution via screen share. 

# Problem: Image Object Detection
## Overview
Build an HTTP REST API in Java or Node.js for a service that ingests user images, analyzes them for object detection, and returns the enhanced content. A user of the full API should be able to:
- Upload an optionally labeled image and run image object detection on it.
- Retrieve all images and any metadata obtained from their analyses.
- Search for images based on detected objects.

## Object Detection
Image object detection can be performed using any 3rd-party API of your choosing (such as [Imagga](https://imagga.com/), or with a process managed by your backend. The only requirement is that it must return a list of object names detected within that image.

## Database
- A persistent datastore is required. Any SQL or NoSQL variation is valid.

## API Specification
- All endpoints should implement error handling and return HTTP codes reflecting the level of failure (client vs. system).
- Unit and/or integration tests for your code are preferred.

### POST /images
- Send a JSON request body including an internet URL referencing an image, an optional label for the image, and an optional field to enable object detection.
- Returns an HTTP 200 OK with a JSON response body including the image data, its label (generate one if the user did not provide it), its identifier provided by the persistent data store, and any objects detected (if object detection was enabled).
### GET /images
- Return HTTP 200 OK with JSON response containing all image metadata.
### GET /images?objects=
- Returns an HTTP 200 OK with a JSON response body containing only images that have the detected objects specified in the query parameter.
### GET /images/{imageId}
- Returns HTTP 200 OK with a JSON response containing image metadata for the specified image.
