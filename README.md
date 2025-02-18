# size-adviser-android

## Installation
### Manual Integration
In order to use the SDK please follow the instructions below:
1. Download the sdk AAR file
2. Create a new module based on the downloaded AAR 
3. Add dependency for the new created module File > Project Structure > Dependencies or in the gradle file `implementation project(':size-adviser')`

### Maven - TBA

## Usage
1. Import the SDK
```kotlin
import com.example.size_adviser.SizeAdviser
```

2. Call `promptForAdvice()` to get the proposed size
```kotlin
SizeAdviser().promptForAdvice(context)
```