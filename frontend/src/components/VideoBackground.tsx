import { useState, useRef, useEffect } from 'react';

// Video background with instant switching between videos
function VideoBackground() {
  const [currentVideo, setCurrentVideo] = useState(0);
  const video1Ref = useRef<HTMLVideoElement>(null);
  const video2Ref = useRef<HTMLVideoElement>(null);
  
  const videos = [
    '/videos/doctors.mp4',
    '/videos/doctors1.mp4',
  ];

  // Preload and prepare next video
  useEffect(() => {
    const nextVideoIndex = (currentVideo + 1) % videos.length;
    const nextVideoRef = currentVideo === 0 ? video2Ref : video1Ref;
    
    if (nextVideoRef.current) {
      nextVideoRef.current.src = videos[nextVideoIndex];
      nextVideoRef.current.load();
      // Preload the video so it's ready
      nextVideoRef.current.currentTime = 0;
    }
  }, [currentVideo]);

  // When video ends, instantly switch to next
  const handleVideoEnd = () => {
    const nextVideoRef = currentVideo === 0 ? video2Ref : video1Ref;
    
    // Start next video immediately
    if (nextVideoRef.current) {
      nextVideoRef.current.play();
    }
    
    // Switch active video
    setCurrentVideo((prev) => (prev + 1) % videos.length);
  };

  return (
    <div className="video-background">
      {/* Video 1 */}
      <video 
        ref={video1Ref}
        autoPlay 
        muted 
        playsInline
        onEnded={handleVideoEnd}
        style={{
          display: currentVideo === 0 ? 'block' : 'none',
        }}
      >
        <source src={videos[0]} type="video/mp4" />
      </video>

      {/* Video 2 */}
      <video 
        ref={video2Ref}
        muted 
        playsInline
        style={{
          display: currentVideo === 1 ? 'block' : 'none',
        }}
      >
        <source src={videos[1]} type="video/mp4" />
      </video>
    </div>
  );
}

export default VideoBackground;