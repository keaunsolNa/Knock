import Image from 'next/image';
import Link from 'next/link';

export default function Home() {
  return (
    <div>
      <Image
        src={'/images/logo_potrait.png'}
        alt="Knock의 로고"
        width={256}
        height={256}
      />

      <Link href={'/movie'}>/movie</Link>
    </div>
  );
}
