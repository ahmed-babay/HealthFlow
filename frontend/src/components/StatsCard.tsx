import { Card } from 'react-bootstrap';

// Props interface
interface StatsCardProps {
  icon: string;
  title: string;
  value: string | number;
  subtitle?: string;
}

// Stats card component - shows a stat with icon
function StatsCard({ icon, title, value, subtitle }: StatsCardProps) {
  return (
    <Card className="glass-card h-100">
      <Card.Body className="p-4">
        <div className="d-flex align-items-center mb-3">
          <span style={{ fontSize: '32px' }} className="me-3">{icon}</span>
          <h6 className="text-white-50 mb-0 text-uppercase" style={{ fontSize: '12px' }}>
            {title}
          </h6>
        </div>
        <h2 className="text-white fw-bold mb-1">{value}</h2>
        {subtitle && <p className="text-white-50 mb-0" style={{ fontSize: '14px' }}>{subtitle}</p>}
      </Card.Body>
    </Card>
  );
}

export default StatsCard;
