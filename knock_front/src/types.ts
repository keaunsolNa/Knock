export interface ICategory {
  categoryId: string;
  categoryNm: string;
  movies: string[];
}

export interface ISearch {
  searchTitle: string;
  searchCategory: string;
}

export interface ICategoryLevelTwo {
  id: string;
  nm: string;
  parentNm: string;
  favoriteUsers: string[];
}

export interface IMovie {
  movieId: string;
  movieNm: string;
  openingTime: string;
  reservationLink: string[];
  posterBase64: string;
  directors: string[];
  actors: string[];
  companyNm: string[];
  categoryLevelTwo: ICategoryLevelTwo[];
  runningTime: number;
  plot: string;
  favorites: string[];
}

export interface IPerformingArts {
  id: string;
  name: string;
  from: string; // 개봉일
  to: string; // 마감일
  directors: string[]; // 제작자
  actors: string[]; // 출연진
  companyNm: string[]; // 회사
  holeNm: string; // 공연장
  poster: string;
  story: string; // 스토리 소개
  styurls: string[]; // 소개 포스터들
  area: string; // 공연 지역
  prfState: 'UPCOMING' | 'ONGOING' | 'COMPLETED' | 'OPEN_RUN' | 'LIMITED_RUN' | 'CLOSING_SOON' | 'UNKNOWN'; // 공연 상태
  dtguidance: string[]; // 공연 시간
  relates: string[]; // 예매처
  runningTime: string;
  categoryLevelTwo: // 장르
  | 'THEATER'
    | 'MUSICAL'
    | 'CLASSICAL'
    | 'KOREAN_TRADITIONAL'
    | 'POPULAR_MUSIC'
    | 'WESTERN_KOREAN_DANCE'
    | 'POPULAR_DANCE'
    | 'CIRCUS_MAGIC'
    | 'COMPLEX'
    | 'UNKNOWN';
  favorites: string[]; // 구독자
}

export interface IUser {
  id: string;
  name: string;
  email: string;
  nickName: string;
  picture: string;
  loginType: 'KAKAO' | 'GOOGLE' | 'NAVER' | 'GUEST';
  role: string;
  favoriteLevelOne: 'MOVIE' | 'PERFORMING_ARTS';
  alarmTimings: string[];
  lastLoginTime: string;
  subscribeList: ISubList;
}

export interface ISubList {
  MOVIE: IMovie[];
  PERFORMING_ARTS: any;
}
