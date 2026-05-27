// API Configuration
const API_BASE_URL = 'https://hospital-management-management-training.onrender.com/api/patients';

// Utility Functions
class NotificationManager {
    show(message, type = 'success', duration = 3000) {
        const notification = document.getElementById('notification');
        notification.textContent = message;
        notification.className = `notification ${type} show`;
        
        setTimeout(() => {
            notification.classList.remove('show');
        }, duration);
    }
}

const notificationManager = new NotificationManager();

// API Handler
class APIHandler {
    static async request(endpoint, method = 'GET', data = null) {
        try {
            const options = {
                method,
                headers: {
                    'Content-Type': 'application/json',
                },
                mode: 'cors',
            };

            if (data) {
                options.body = JSON.stringify(data);
            }

            const response = await fetch(`${API_BASE_URL}${endpoint}`, options);

            if (!response.ok) {
                throw new Error(`HTTP Error: ${response.status}`);
            }

            return await response.json();
        } catch (error) {
            console.error('API Error:', error);
            throw error;
        }
    }

    static async getPatients() {
        return this.request('/patients');
    }

    static async addPatient(patientData) {
        return this.request('/patients', 'POST', patientData);
    }

    static async getDoctors() {
        return this.request('/doctors');
    }

    static async addDoctor(doctorData) {
        return this.request('/doctors', 'POST', doctorData);
    }

    static async getAppointments() {
        return this.request('/appointments');
    }

    static async addAppointment(appointmentData) {
        return this.request('/appointments', 'POST', appointmentData);
    }

    static async getAdmissions() {
        return this.request('/admissions');
    }

    static async addAdmission(admissionData) {
        return this.request('/admissions', 'POST', admissionData);
    }

    static async getWards() {
        return this.request('/wards');
    }

    static async getBeds() {
        return this.request('/beds');
    }

    static async checkHealth() {
        try {
            const response = await this.request('/health');
            return true;
        } catch {
            return false;
        }
    }
}

// Data Renderers
class DataRenderer {
    static renderPatients(patients) {
        const list = document.getElementById('patientsList');
        
        if (!patients || patients.length === 0) {
            list.innerHTML = '<p class="placeholder">No patients found. Add one to get started.</p>';
            return;
        }

        list.innerHTML = patients.map(patient => `
            <div class="data-card">
                <div class="card-header">
                    <div class="card-id">P-${patient.patientId}</div>
                </div>
                <div class="card-field">
                    <span class="card-label">Name:</span>
                    <span class="card-value">${this.escapeHtml(patient.name)}</span>
                </div>
                <div class="card-field">
                    <span class="card-label">Age:</span>
                    <span class="card-value">${patient.age} years</span>
                </div>
                <div class="card-field">
                    <span class="card-label">Disease:</span>
                    <span class="card-value">${this.escapeHtml(patient.disease)}</span>
                </div>
            </div>
        `).join('');
    }

    static renderDoctors(doctors) {
        const list = document.getElementById('doctorsList');
        
        if (!doctors || doctors.length === 0) {
            list.innerHTML = '<p class="placeholder">No doctors found. Add one to get started.</p>';
            return;
        }

        list.innerHTML = doctors.map(doctor => `
            <div class="data-card">
                <div class="card-header">
                    <div class="card-id">D-${doctor.doctorId}</div>
                </div>
                <div class="card-field">
                    <span class="card-label">Name:</span>
                    <span class="card-value">${this.escapeHtml(doctor.name)}</span>
                </div>
                <div class="card-field">
                    <span class="card-label">Specialization:</span>
                    <span class="card-value">${this.escapeHtml(doctor.specialization)}</span>
                </div>
            </div>
        `).join('');
    }

    static renderAppointments(appointments) {
        const list = document.getElementById('appointmentsList');
        
        if (!appointments || appointments.length === 0) {
            list.innerHTML = '<p class="placeholder">No appointments found. Book one to get started.</p>';
            return;
        }

        list.innerHTML = appointments.map(apt => `
            <div class="data-card">
                <div class="card-header">
                    <div class="card-id">A-${apt.appointmentId}</div>
                </div>
                <div class="card-field">
                    <span class="card-label">Date:</span>
                    <span class="card-value">${this.formatDate(apt.appointmentDate)}</span>
                </div>
                <div class="card-field">
                    <span class="card-label">Patient:</span>
                    <span class="card-value">${this.escapeHtml(apt.patientName)} (Age: ${apt.age})</span>
                </div>
                <div class="card-field">
                    <span class="card-label">Disease:</span>
                    <span class="card-value">${this.escapeHtml(apt.disease)}</span>
                </div>
                <div class="card-field">
                    <span class="card-label">Doctor:</span>
                    <span class="card-value">${this.escapeHtml(apt.doctorName)}</span>
                </div>
                <div class="card-field">
                    <span class="card-label">Specialization:</span>
                    <span class="card-value">${this.escapeHtml(apt.specialization)}</span>
                </div>
            </div>
        `).join('');
    }

    static renderAdmissions(admissions) {
        const list = document.getElementById('admissionsList');
        
        if (!admissions || admissions.length === 0) {
            list.innerHTML = '<p class="placeholder">No admissions found. Process one to get started.</p>';
            return;
        }

        list.innerHTML = admissions.map(admission => {
            const severityClass = this.getSeverityBadgeClass(admission.severity);
            return `
                <div class="data-card">
                    <div class="card-header">
                        <div class="card-id">Adm-${admission.admissionId}</div>
                        <span class="card-badge ${severityClass}">${admission.severity?.toUpperCase()}</span>
                    </div>
                    <div class="card-field">
                        <span class="card-label">Date:</span>
                        <span class="card-value">${this.formatDate(admission.admissionDate)}</span>
                    </div>
                    <div class="card-field">
                        <span class="card-label">Patient:</span>
                        <span class="card-value">${this.escapeHtml(admission.patientName)} (${admission.age})</span>
                    </div>
                    <div class="card-field">
                        <span class="card-label">Disease:</span>
                        <span class="card-value">${this.escapeHtml(admission.disease)}</span>
                    </div>
                    <div class="card-field">
                        <span class="card-label">Status:</span>
                        <span class="card-value">${admission.admissionStatus}</span>
                    </div>
                    ${admission.wardName ? `
                    <div class="card-field">
                        <span class="card-label">Ward:</span>
                        <span class="card-value">${this.escapeHtml(admission.wardName)}</span>
                    </div>
                    ` : ''}
                    ${admission.bedNumber ? `
                    <div class="card-field">
                        <span class="card-label">Bed:</span>
                        <span class="card-value">${this.escapeHtml(admission.bedNumber)}</span>
                    </div>
                    ` : ''}
                    ${admission.notes ? `
                    <div class="card-field">
                        <span class="card-label">Notes:</span>
                        <span class="card-value">${this.escapeHtml(admission.notes)}</span>
                    </div>
                    ` : ''}
                </div>
            `;
        }).join('');
    }

    static renderWards(wards) {
        const list = document.getElementById('wardsList');
        
        if (!wards || wards.length === 0) {
            list.innerHTML = '<p class="placeholder">No wards found.</p>';
            return;
        }

        list.innerHTML = wards.map(ward => `
            <div class="data-card">
                <div class="card-header">
                    <div class="card-id">W-${ward.wardId}</div>
                </div>
                <div class="card-field">
                    <span class="card-label">Name:</span>
                    <span class="card-value">${this.escapeHtml(ward.wardName)}</span>
                </div>
                <div class="card-field">
                    <span class="card-label">Type:</span>
                    <span class="card-value">${this.escapeHtml(ward.wardType)}</span>
                </div>
                <div class="card-field">
                    <span class="card-label">Total Beds:</span>
                    <span class="card-value">${ward.totalBeds}</span>
                </div>
                <div class="card-field">
                    <span class="card-label">Severity Rank:</span>
                    <span class="card-value">${ward.severityRank}</span>
                </div>
            </div>
        `).join('');
    }

    static renderBeds(beds) {
        const list = document.getElementById('bedsList');
        
        if (!beds || beds.length === 0) {
            list.innerHTML = '<p class="placeholder">No beds found.</p>';
            return;
        }

        list.innerHTML = beds.map(bed => {
            const availabilityClass = bed.isAvailable ? 'badge-success' : 'badge-danger';
            const availabilityText = bed.isAvailable ? 'Available' : 'Occupied';
            return `
                <div class="data-card">
                    <div class="card-header">
                        <div class="card-id">B-${bed.bedId}</div>
                        <span class="card-badge ${availabilityClass}">${availabilityText}</span>
                    </div>
                    <div class="card-field">
                        <span class="card-label">Bed Number:</span>
                        <span class="card-value">${this.escapeHtml(bed.bedNumber)}</span>
                    </div>
                    <div class="card-field">
                        <span class="card-label">Ward:</span>
                        <span class="card-value">${this.escapeHtml(bed.wardName)}</span>
                    </div>
                    <div class="card-field">
                        <span class="card-label">Ward Type:</span>
                        <span class="card-value">${this.escapeHtml(bed.wardType)}</span>
                    </div>
                </div>
            `;
        }).join('');
    }

    static formatDate(dateStr) {
        if (!dateStr) return 'N/A';
        const date = new Date(dateStr);
        return date.toLocaleDateString('en-US', { 
            year: 'numeric', 
            month: 'short', 
            day: 'numeric' 
        });
    }

    static getSeverityBadgeClass(severity) {
        if (!severity) return 'badge-warning';
        const sev = severity.toLowerCase();
        if (sev.includes('critical') || sev.includes('high')) return 'badge-danger';
        if (sev.includes('medium')) return 'badge-warning';
        return 'badge-success';
    }

    static escapeHtml(text) {
        if (!text) return '';
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
}

// Data Manager
class DataManager {
    static async loadPatients() {
        try {
            const patients = await APIHandler.getPatients();
            DataRenderer.renderPatients(patients);
        } catch (error) {
            notificationManager.show('Failed to load patients', 'error');
            console.error('Error loading patients:', error);
        }
    }

    static async loadDoctors() {
        try {
            const doctors = await APIHandler.getDoctors();
            DataRenderer.renderDoctors(doctors);
        } catch (error) {
            notificationManager.show('Failed to load doctors', 'error');
            console.error('Error loading doctors:', error);
        }
    }

    static async loadAppointments() {
        try {
            const appointments = await APIHandler.getAppointments();
            DataRenderer.renderAppointments(appointments);
        } catch (error) {
            notificationManager.show('Failed to load appointments', 'error');
            console.error('Error loading appointments:', error);
        }
    }

    static async loadAdmissions() {
        try {
            const admissions = await APIHandler.getAdmissions();
            DataRenderer.renderAdmissions(admissions);
        } catch (error) {
            notificationManager.show('Failed to load admissions', 'error');
            console.error('Error loading admissions:', error);
        }
    }

    static async loadWards() {
        try {
            const wards = await APIHandler.getWards();
            DataRenderer.renderWards(wards);
        } catch (error) {
            notificationManager.show('Failed to load wards', 'error');
            console.error('Error loading wards:', error);
        }
    }

    static async loadBeds() {
        try {
            const beds = await APIHandler.getBeds();
            DataRenderer.renderBeds(beds);
        } catch (error) {
            notificationManager.show('Failed to load beds', 'error');
            console.error('Error loading beds:', error);
        }
    }
}

// Form Handlers
class FormHandler {
    static setupPatientForm() {
        const form = document.getElementById('patientForm');
        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            
            const patientData = {
                patientId: parseInt(document.getElementById('patientId').value),
                name: document.getElementById('patientName').value,
                age: parseInt(document.getElementById('patientAge').value),
                disease: document.getElementById('patientDisease').value,
            };

            try {
                await APIHandler.addPatient(patientData);
                notificationManager.show('Patient added successfully!', 'success');
                form.reset();
                await DataManager.loadPatients();
            } catch (error) {
                notificationManager.show('Failed to add patient', 'error');
                console.error('Error adding patient:', error);
            }
        });
    }

    static setupDoctorForm() {
        const form = document.getElementById('doctorForm');
        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            
            const doctorData = {
                doctorId: parseInt(document.getElementById('doctorId').value),
                name: document.getElementById('doctorName').value,
                specialization: document.getElementById('doctorSpec').value,
            };

            try {
                await APIHandler.addDoctor(doctorData);
                notificationManager.show('Doctor added successfully!', 'success');
                form.reset();
                await DataManager.loadDoctors();
            } catch (error) {
                notificationManager.show('Failed to add doctor', 'error');
                console.error('Error adding doctor:', error);
            }
        });
    }

    static setupAppointmentForm() {
        const form = document.getElementById('appointmentForm');
        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            
            const appointmentData = {
                appointmentId: parseInt(document.getElementById('appointmentId').value),
                patientId: parseInt(document.getElementById('appointmentPatientId').value),
                doctorId: parseInt(document.getElementById('appointmentDoctorId').value),
                appointmentDate: document.getElementById('appointmentDate').value,
            };

            try {
                await APIHandler.addAppointment(appointmentData);
                notificationManager.show('Appointment booked successfully!', 'success');
                form.reset();
                await DataManager.loadAppointments();
            } catch (error) {
                notificationManager.show('Failed to book appointment', 'error');
                console.error('Error booking appointment:', error);
            }
        });
    }

    static setupAdmissionForm() {
        const form = document.getElementById('admissionForm');
        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            
            const admissionData = {
                patientId: parseInt(document.getElementById('admissionPatientId').value),
                severity: document.getElementById('admissionSeverity').value,
                notes: document.getElementById('admissionNotes').value || null,
            };

            try {
                await APIHandler.addAdmission(admissionData);
                notificationManager.show('Admission processed successfully!', 'success');
                form.reset();
                await DataManager.loadAdmissions();
                await DataManager.loadBeds();
            } catch (error) {
                notificationManager.show('Failed to process admission', 'error');
                console.error('Error processing admission:', error);
            }
        });
    }
}

// Tab Navigation
class TabManager {
    static setup() {
        const navButtons = document.querySelectorAll('.nav-btn');
        
        navButtons.forEach(button => {
            button.addEventListener('click', () => {
                const tabName = button.dataset.tab;
                this.showTab(tabName);
                
                // Update active button
                navButtons.forEach(btn => btn.classList.remove('active'));
                button.classList.add('active');
            });
        });
    }

    static showTab(tabName) {
        const tabContents = document.querySelectorAll('.tab-content');
        tabContents.forEach(tab => tab.classList.remove('active'));
        
        const activeTab = document.getElementById(`${tabName}-tab`);
        if (activeTab) {
            activeTab.classList.add('active');
            this.loadTabData(tabName);
        }
    }

    static async loadTabData(tabName) {
        switch(tabName) {
            case 'patients':
                await DataManager.loadPatients();
                break;
            case 'doctors':
                await DataManager.loadDoctors();
                break;
            case 'appointments':
                await DataManager.loadAppointments();
                break;
            case 'admissions':
                await DataManager.loadAdmissions();
                break;
            case 'wards':
                await Promise.all([
                    DataManager.loadWards(),
                    DataManager.loadBeds()
                ]);
                break;
        }
    }
}

// Connection Status Monitor
class ConnectionMonitor {
    static async start() {
        setInterval(async () => {
            const isOnline = await APIHandler.checkHealth();
            const statusDot = document.getElementById('connectionStatus');
            const statusText = document.getElementById('statusText');
            
            if (isOnline) {
                statusDot.classList.remove('offline');
                statusDot.classList.add('online');
                statusText.textContent = 'Connected';
            } else {
                statusDot.classList.remove('online');
                statusDot.classList.add('offline');
                statusText.textContent = 'Disconnected';
            }
        }, 5000);
    }
}

// Refresh Button Handlers
class RefreshHandler {
    static setup() {
        document.getElementById('refreshPatients').addEventListener('click', () => DataManager.loadPatients());
        document.getElementById('refreshDoctors').addEventListener('click', () => DataManager.loadDoctors());
        document.getElementById('refreshAppointments').addEventListener('click', () => DataManager.loadAppointments());
        document.getElementById('refreshAdmissions').addEventListener('click', () => DataManager.loadAdmissions());
        document.getElementById('refreshWards').addEventListener('click', () => DataManager.loadWards());
        document.getElementById('refreshBeds').addEventListener('click', () => DataManager.loadBeds());
    }
}

// Initialize Application
function initializeApp() {
    console.log('Initializing Hospital Management System...');
    
    // Setup navigation
    TabManager.setup();
    
    // Setup forms
    FormHandler.setupPatientForm();
    FormHandler.setupDoctorForm();
    FormHandler.setupAppointmentForm();
    FormHandler.setupAdmissionForm();
    
    // Setup refresh buttons
    RefreshHandler.setup();
    
    // Start monitoring connection
    ConnectionMonitor.start();
    
    // Load initial data
    DataManager.loadPatients();
    
    notificationManager.show('Application loaded successfully', 'success', 2000);
    
    console.log('Hospital Management System initialized');
}

// Start app when DOM is ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initializeApp);
} else {
    initializeApp();
}
