// Video background component for the entire app
function VideoBackground() {
  return (
    <div className="video-background">
      <video autoPlay loop muted playsInline>
        {/* Video should be in public/videos/ folder */}
        <source 
          src="/videos/doctors.mp4" 
          type="video/mp4" 
        />
        {/* Fallback if video doesn't load */}
        Your browser does not support video backgrounds.
      </video>
    </div>
  );
}

export default VideoBackground;

