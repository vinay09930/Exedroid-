# ExeDroid Clean Architecture & Backend Refactor

## Layers Overview

1. **Presentation Layer** (`com.example.ui`)
   - Jetpack Compose screens and components.
   - ViewModels managing UI state using `StateFlow`.
   - **Rule**: No business logic. Delegates actions to Use Cases.

2. **Domain Layer** (`com.example.domain`)
   - Pure Kotlin module (no Android framework dependencies).
   - Contains:
     - **Models**: Enterprise data models (`AppInfo`, `InstanceInfo`, `ResourceStats`).
     - **Repository Interfaces**: Contracts for data operations.
     - **Use Cases**: Encapsulates specific business rules (e.g., `LaunchInstanceUseCase`, `AnalyzeDeviceUseCase`).
   - **Rule**: Completely independent of data sources or UI.

3. **Data Layer** (`com.example.data`)
   - Implementations of the Repository Interfaces from the Domain Layer.
   - Connects to local databases (Room), DataStore, or external APIs.
   - Contains:
     - Local database entities and DAOs.
     - Network APIs (Retrofit/Ktor).
     - Data mapping logic (DTOs to Domain Models).

4. **Platform Layer** (`com.example.core.platform`)
   - Manages device-level capabilities.
   - Implementations of platform-specific interfaces (e.g., `IResourceMonitor`, `IThermalProtector`, `IDiagnosticsEngine`).

5. **Runtime Layer** (`com.example.core.managers.runtimes`)
   - Manages isolated environments (e.g., Linux, Windows, macOS compatibility layers).
   - Provides an API for installing, patching, and running executables.

6. **Build System Layer** (`com.example.core.managers.build`)
   - Handles the packaging and generation of APKs/AABs.
   - Responsible for signing and bundling resources.

## Folder Structure

```
/app/src/main/java/com/example
├── core
│   ├── intelligence         # AI and telemetry services
│   ├── managers             # Runtime and Build system management
│   └── platform             # Device resource/thermal management
├── data
│   ├── local                # Room Database, DataStore
│   ├── network              # API clients
│   └── repository           # Repository implementations
├── di                       # Dependency Injection Container (or Hilt)
├── domain
│   ├── models               # Pure Kotlin data classes
│   ├── repository           # Repository interfaces
│   └── usecase              # Business logic (e.g., GetDeviceStatsUseCase)
├── ui
│   ├── ai                   # AI Systems UI
│   ├── device               # Device Analysis UI
│   ├── home                 # Dashboard UI
│   ├── platform             # Platform Capabilities UI
│   └── project              # Project management UI
└── workers                  # WorkManager background tasks
```

## Dependency Graph
- `Presentation` -> `Domain`
- `Data` -> `Domain`
- `Platform` -> `Domain`
- `Runtime` -> `Domain`
- `Build System` -> `Domain`
- `DI` -> Glues everything together (injects implementations into `Presentation` and `Domain`).

## Interface Definitions & Service Contracts

### Domain Layer (Contracts)
```kotlin
interface DeviceRepository {
    fun getDeviceSpecs(): Flow<DeviceSpecs>
}

interface AppRepository {
    suspend fun getInstalledApps(): List<AppInfo>
}

interface PlatformService {
    fun monitorResources(): Flow<ResourceStats>
    fun monitorThermals(): Flow<ThermalState>
    suspend fun runDiagnostics(): Flow<String>
}
```

### Use Cases
```kotlin
class MonitorDeviceHealthUseCase(
    private val platformService: PlatformService
) {
    operator fun invoke(): Flow<DeviceHealthStatus> {
        // Combines resources and thermals
    }
}
```

## Next Steps for Implementation
1. Extract existing `PlatformManager` and `DeviceAnalysis` logic into domain use cases.
2. Introduce proper interface definitions for all Core components.
3. Replace direct instantiation in ViewModels with UseCase injection.
4. Integrate WorkManager for background runtime synchronizations.
