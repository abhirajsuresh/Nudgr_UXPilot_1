# Nudgr - Focus & Productivity App

Nudgr is a productivity mobile app designed to help users reclaim focus and build deep work habits by interrupting distraction cycles.

## Features

- **Gentle Reminders**: App runs in background and reminds users to put their phone down when unlocked
- **Customizable Timers**: Set Deep Work Duration and Reminder Interval
- **Motivational Images**: Add unlimited images that appear randomly during reminders
- **Session Tracking**: Log performance and track progress over time
- **Smart Actions**: Lock phone, snooze, or extend session time

## Architecture

- **Frontend**: Android native with Jetpack Compose
- **Database**: Room for local data storage
- **Background**: Foreground service for session management
- **Navigation**: Navigation Compose with single-activity architecture
- **DI**: Hilt for dependency injection

## Project Structure

```
app/src/main/java/com/nudgr/
├── core/
│   └── ui/
│       ├── components/     # Reusable UI components
│       └── theme/         # Design system and theming
├── data/
│   ├── local/
│   │   ├── database/      # Room database setup
│   │   ├── dao/          # Data access objects
│   │   └── entity/       # Database entities
│   └── repository/       # Repository pattern implementation
├── di/                   # Hilt dependency injection modules
├── receiver/            # Broadcast receivers for system events
├── service/             # Background services
└── ui/
    ├── welcome/         # Onboarding screens
    ├── checklist/        # Setup checklist
    ├── images/          # Image management
    ├── timers/          # Timer configuration
    ├── dashboard/        # Main dashboard
    ├── permissions/     # Permission management
    └── reminder/        # Full-screen reminder overlay
```

## Setup

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Run on device or emulator

### Environment Variables

This project uses environment variables to handle sensitive API keys. To get started, you'll need to create a `.cursor/mcp.json` file.

1.  Copy the example configuration file:
    ```bash
    cp .cursor/mcp.json.example .cursor/mcp.json
    ```
2.  The `.cursor/mcp.json` file is configured to read the following environment variables. Ensure these are set in your development environment (e.g., in your shell profile or using a `.env` file loaded by your shell):

    *   `ANTHROPIC_API_KEY`
    *   `PERPLEXITY_API_KEY`
    *   `OPENAI_API_KEY`
    *   `GOOGLE_API_KEY`
    *   `XAI_API_KEY`
    *   `OPENROUTER_API_KEY`
    *   `MISTRAL_API_KEY`
    *   `AZURE_OPENAI_API_KEY`
    *   `OLLAMA_API_KEY`

The application will load these variables at runtime. The `.cursor/mcp.json` file itself is excluded from version control via `.gitignore`.

## Permissions

The app requires several permissions:
- **Notifications**: For reminder notifications
- **Battery Optimization**: To prevent background killing
- **Overlay**: For full-screen reminders
- **Storage**: For image access
- **Device Admin**: For lock phone feature (optional)

## Development Status

This is the first draft implementation with:
- ✅ Project structure and dependencies
- ✅ Design system and theming
- ✅ Navigation setup
- ✅ Database layer (Room)
- ✅ Core screens (Welcome, Checklist, Images, Timers, Dashboard, Permissions)
- ✅ Background service foundation
- ✅ Permission handling
- ✅ Basic reminder overlay

## Next Steps

- Implement session engine logic
- Add analytics and event tracking
- Implement image shuffle bag
- Add reports and insights screens
- Add authentication (Google Sign-In)
- Add preferences management
- Add comprehensive testing

## License

Private project - All rights reserved
