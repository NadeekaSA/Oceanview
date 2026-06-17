/**
 * Help Manager - Context-Sensitive Documentation System
 */

const helpContent = {
    'dashboard': {
        title: 'Dashboard Overview',
        sections: [
            { icon: '📊', title: 'Stats Cards', text: 'View live counts of available and occupied rooms at a glance.' },
            { icon: '🕒', title: 'Recent Activity', text: 'Track the latest guest registrations and booking changes as they happen.' }
        ]
    },
    'guests': {
        title: 'Guest Management',
        sections: [
            { icon: '🔍', title: 'Finding Guests', text: 'Use the search bar to filter by name or ID card number instantly.' },
            { icon: '➕', title: 'Registration', text: 'Click "Add New Guest" to open the registration form. Ensure all details are accurate.' },
            { icon: '📄', title: 'Profiles', text: 'Click the "View" button to see full guest history and details in a new tab.' }
        ]
    },
    'rooms': {
        title: 'Room Status & Rates',
        sections: [
            { icon: '🛌', title: 'Occupancy', text: 'Rooms are Color-coded: Green (Available), Red (Occupied), Blue (Reserved/Booked), Orange (Maintenance).' },
            { icon: '🛠️', title: 'Maintenance', text: 'Set a room to Maintenance to block it for 10 minutes. It reverts to Available automatically.' },
            { icon: '📅', title: 'Quick Booking', text: 'Click "Book" on an Available room to jump directly to the reservation screen.' }
        ]
    },
    'reservations': {
        title: 'Reservation Portal',
        sections: [
            { icon: '📅', title: 'Booking Flow', text: 'Select a guest and room, then pick dates. Total cost is calculated automatically.' },
            { icon: '🔑', title: 'Check-In', text: 'New bookings start as "BOOKED". Click the "Check In" button when the guest arrives.' },
            { icon: '🚪', title: 'Check-Out', text: 'When a guest leaves, click "Check Out" to finalize and generate the printable bill.' },
            { icon: '🔎', title: 'Search', text: 'Search by Reservation #, Room, or Guest ID to find any booking quickly.' }
        ]
    },
    'admin': {
        title: 'Administrative Control',
        sections: [
            { icon: '👥', title: 'Staff Accounts', text: 'Create or remove receptionist accounts. Note: Role cannot be changed after creation.' },
            { icon: '🏨', title: 'Room Config', text: 'Add new rooms or update nightly rates (LKR) to reflect seasonal changes.' },
            { icon: '📈', title: 'Reporting', text: 'Extract revenue and occupancy reports. Data can be exported to CSV or Excel.' }
        ]
    }
};

function initHelp(pageKey) {
    if (!helpContent[pageKey]) return;

    // Create Floating Button if not exists
    if (!document.querySelector('.help-fab')) {
        const fab = document.createElement('button');
        fab.className = 'help-fab';
        fab.innerHTML = '?';
        fab.title = 'Need Help?';
        fab.onclick = () => showHelpModal(pageKey);
        document.body.appendChild(fab);
    }

    // Create Modal if not exists
    if (!document.querySelector('.help-modal')) {
        const modal = document.createElement('div');
        modal.className = 'help-modal';
        modal.id = 'helpModal';
        modal.innerHTML = `
            <div class="help-modal-content">
                <div class="help-modal-header">
                    <h2 id="helpTitle">Support Guide</h2>
                    <span class="help-close" onclick="closeHelp()">&times;</span>
                </div>
                <div id="helpBody"></div>
                <div class="help-footer">
                    <a href="help.html" class="btn btn-sm btn-accent">View Full Documentation</a>
                </div>
            </div>
        `;
        document.body.appendChild(modal);
        
        // Close on outside click
        modal.onclick = (e) => { if (e.target === modal) closeHelp(); };
    }
}

function showHelpModal(pageKey) {
    const data = helpContent[pageKey];
    document.getElementById('helpTitle').innerText = data.title;
    
    let html = '';
    data.sections.forEach(s => {
        html += `
            <div class="help-section-item">
                <h4><span>${s.icon}</span> ${s.title}</h4>
                <p>${s.text}</p>
            </div>
        `;
    });
    
    document.getElementById('helpBody').innerHTML = html;
    document.getElementById('helpModal').style.display = 'flex';
}

function closeHelp() {
    document.getElementById('helpModal').style.display = 'none';
}

// Sidebar Integration
document.addEventListener('DOMContentLoaded', () => {
    const nav = document.querySelector('.sidebar-nav');
    if (nav) {
        const helpLink = document.createElement('a');
        helpLink.href = 'help.html';
        helpLink.className = 'nav-item';
        helpLink.innerHTML = 'System Help & Info';
        nav.appendChild(helpLink);
    }
});
