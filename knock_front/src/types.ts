export interface ICategory {
  categoryId: string;
  categoryNm: string;
  movies: string[];
}

export interface IUser {
  id: string;
  nme: string;
}
export interface ICategoryLevelTwo {
  id: string;
  nm: string;
  parentNm: string;
  favoriteUsers: IUser[];
}

export interface IMovie {
  movieId: string;
  movieNm: string;
  openingTime: string;
  reservationLink: string[];
  posterBase64: string;
  directors: string[];
  actors: string[];
  companyNm: string;
  categoryLevelTwo: ICategoryLevelTwo[];
  runningTime: number;
  plot: string;
  favorites: IUser[];
  koficcode: string;
}
