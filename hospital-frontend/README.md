# Hospital Management System - Web Frontend

A modern, responsive HTML/CSS/JavaScript frontend for the Hospital Management System. This web-based interface replaces the legacy JavaFX desktop application and provides a cloud-ready solution deployable to Vercel.

## Features

✨ **Core Features**
- 👥 **Patients Management** - Add and view patient records with disease information
- 👨‍⚕️ **Doctors Management** - Manage doctor profiles with specializations
- 📅 **Appointments** - Schedule and view appointments between patients and doctors
- 🏥 **Admissions** - Process patient admissions with severity-based bed allocation
- 🛏️ **Wards & Beds** - View hospital ward structure and bed availability

🎨 **User Experience**
- Responsive design that works on desktop, tablet, and mobile devices
- Real-time connection monitoring to backend API
- Intuitive tab-based navigation
- Toast notifications for user feedback
- Data cards with organized information display
- Dark mode ready styling

🚀 **Technical Features**
- Pure HTML5/CSS3/Vanilla JavaScript (no dependencies)
- RESTful API integration with backend microservice
- CORS-enabled for cross-origin requests
- Vercel-ready deployment configuration
- Modern ES6+ JavaScript patterns

## Architecture

### Frontend Stack
- **HTML5** - Semantic markup structure
- **CSS3** - Flexbox, Grid, and modern CSS features
- **Vanilla JavaScript** - No external dependencies for production

### Backend Integration
- Connects to Hospital Management System REST API
- Base URL: `https://hospital-management-management-training.onrender.com/api/patients`
- Implements all CRUD operations for core entities

### API Endpoints Used
- `GET /patients` - Retrieve all patients
- `POST /patients` - Add new patient
- `GET /doctors` - Retrieve all doctors
- `POST /doctors` - Add new doctor
- `GET /appointments` - Retrieve all appointments
- `POST /appointments` - Book appointment
- `GET /admissions` - Retrieve all admissions
- `POST /admissions` - Process admission
- `GET /wards` - Retrieve ward information
- `GET /beds` - Retrieve bed information
- `GET /health` - Check API health status

## Quick Start

### Local Development

1. **Clone or download the project**
   ```bash
   cd hospital-frontend
   ```

2. **Start a local server**
   ```bash
   # Using Python 3
   python -m http.server 8000
   
   # Or using npm http-server
   npm install
   npm run dev
   ```

3. **Open in browser**
   ```
   http://localhost:8000
   ```

### Environment Setup

The application connects to the backend API at:
```
https://hospital-management-management-training.onrender.com/api/patients
```

To use a different backend, edit `app.js`:
```javascript
const API_BASE_URL = 'https://your-backend-url/api/patients';
```

## Deployment

### Deploy to Vercel

1. **Install Vercel CLI**
   ```bash
   npm i -g vercel
   ```

2. **Deploy**
   ```bash
   vercel
   ```

3. **Follow prompts**
   - Link to Vercel account
   - Select project directory
   - Confirm deployment settings

### Alternative: GitHub + Vercel

1. Push this repository to GitHub
2. Connect repository to Vercel dashboard
3. Vercel automatically deploys on push

## File Structure

```
hospital-frontend/
├── index.html          # Main HTML structure
├── styles.css          # All styling and responsive design
├── app.js              # JavaScript application logic
├── package.json        # NPM package configuration
├── vercel.json         # Vercel deployment config
├── .gitignore          # Git ignore rules
└── README.md           # This file
```

## Usage Guide

### Adding a Patient
1. Click on **Patients** tab
2. Fill in patient details (ID, Name, Age, Disease)
3. Click **Add Patient**
4. Refresh to see the updated list

### Adding a Doctor
1. Click on **Doctors** tab
2. Enter doctor details (ID, Name, Specialization)
3. Click **Add Doctor**

### Booking an Appointment
1. Click on **Appointments** tab
2. Enter appointment details (ID, Patient ID, Doctor ID, Date)
3. Click **Book Appointment**
4. View all scheduled appointments

### Processing an Admission
1. Click on **Admissions** tab
2. Enter patient ID and severity level
3. Optionally add admission notes
4. Click **Process Admission**
5. System automatically allocates beds based on severity

### Viewing Wards & Beds
1. Click on **Wards & Beds** tab
2. View all hospital wards with capacity information
3. Check bed availability status

## Severity Levels

- **Low** - Consultation only (no bed required)
- **Medium** - General ward admission
- **High** - Emergency ward admission
- **Critical** - ICU admission

## API Response Handling

The application includes comprehensive error handling:

- **Connection failures** - Displayed in status indicator
- **API errors** - Toast notifications with error messages
- **Data validation** - Client-side form validation
- **Graceful degradation** - Continues working with partial data

## Development

### Customization

Edit `styles.css` for custom styling:
```css
:root {
    --primary-color: #2563eb;
    --danger-color: #ef4444;
    /* More colors... */
}
```

Update form fields in `index.html`:
```html
<input type="text" id="fieldName" placeholder="Field" required>
```

Add API calls in `app.js`:
```javascript
static async newMethod() {
    return this.request('/endpoint');
}
```

### Browser Compatibility

- Chrome 90+
- Firefox 88+
- Safari 14+
- Edge 90+

## Troubleshooting

### Connection Shows "Disconnected"
- Check if backend is running
- Verify backend URL in `app.js`
- Check browser console for CORS errors

### Data Not Loading
- Click **Refresh** button for specific section
- Check network tab in browser DevTools
- Ensure backend API is accessible

### Forms Not Submitting
- Verify all required fields are filled
- Check browser console for error messages
- Ensure backend API accepts the data format

## Performance

- Page load time: < 1 second
- No build step required - instant deployment
- Lightweight: Only ~30KB of JavaScript
- Zero external dependencies

## Security Notes

- Frontend communicates only over HTTPS in production
- All API calls are made to secured backend endpoints
- User input is sanitized before display
- No sensitive data stored in localStorage

## Future Enhancements

- [ ] User authentication with JWT tokens
- [ ] Advanced search and filtering
- [ ] Export data to CSV/PDF
- [ ] Appointment reminders
- [ ] Patient medical history timeline
- [ ] Doctor schedules and availability
- [ ] Ward capacity dashboard
- [ ] Mobile app version (React Native)

## Support

For issues or questions:
1. Check the troubleshooting section
2. Review browser console logs
3. Verify backend API connectivity
4. Contact development team

## License

MIT License - Feel free to use and modify

## Credits

- Built with vanilla HTML5/CSS3/JavaScript
- Architecture based on Hospital Management System microservices
- Designed for modern web browsers and Vercel deployment
