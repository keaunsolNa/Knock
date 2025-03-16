import styles from '@/styles/components/performingArts/category-nav.module.scss';
import { performingArtsGenreList, performingArtsGenreToText } from '@/utils/typeToText';
import Link from 'next/link';

export default function CategoryNav() {
  function genreToLink(genre: string): string {
    return genre.toLowerCase().replace(/_(\w)/g, (_, char) => char.toUpperCase());
  }

  return (
    <div className={styles.container}>
      {performingArtsGenreList.map((genre) => (
        <div key={genre} className={styles.genre}>
          <Link href={`/performingArts/${genreToLink(genre)}`}>
            <img src={`/genre/${genre.toLowerCase()}.png`} alt={`${performingArtsGenreToText[genre]} 장르 아이콘`} />
            <div>{performingArtsGenreToText[genre]}</div>
          </Link>
        </div>
      ))}
    </div>
  );
}
