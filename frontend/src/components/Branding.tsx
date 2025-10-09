// Component for branding/marketing content on the left side
function Branding() {
  return (
    <div className="text-center">
      {/* Professional Logo */}
      <div className="professional-logo mb-4">
        <svg width="100" height="100" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
          {/* Circle background */}
          <circle cx="50" cy="50" r="45" fill="rgba(255, 255, 255, 0.2)" />
          
          {/* Medical cross */}
          <rect x="42" y="25" width="16" height="50" fill="white" rx="2" />
          <rect x="25" y="42" width="50" height="16" fill="white" rx="2" />
          
          {/* Heart pulse line */}
          <path 
            d="M 20 50 L 30 50 L 35 40 L 40 60 L 45 50 L 80 50" 
            stroke="rgba(255, 255, 255, 0.5)" 
            strokeWidth="2" 
            fill="none"
          />
        </svg>
      </div>
      
      <h1 className="display-3 fw-bold mb-3">HealthFlow</h1>
      <p className="lead">Your Complete Healthcare Management Solution</p>
    </div>
  );
}

export default Branding;
